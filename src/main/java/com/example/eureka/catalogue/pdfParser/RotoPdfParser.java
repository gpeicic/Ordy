package com.example.eureka.catalogue.pdfParser;

import com.example.eureka.catalogue.CatalogueItem;
import com.example.eureka.exception.ValidationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser za Roto cjenik PDF (dvo-kolumni layout, A4, ~120 stranica).
 *
 * Ključni insights:
 *
 * 1. ITEM LINIJA: indent točno 5 razmaka, pa odmah digit (šifra artikla).
 *    NAME LINIJA: sve ostalo (indent >= 11 razmaka).
 *
 * 2. PRAVA CIJENA vs LITRAŽA/VOLUMEN u nazivu:
 *    Regex koristi lookahead — cijena je X,XX koji je ankoriziran sljedećim
 *    kodom ili krajem linije:
 *      (\d{3,8})\s+(.+?)\s+(\d{1,4},\d{2})(?=\s+\d{3,8}|\s*$)
 *    Tako "GIN PANAREA ISLAND 0,7 (6)* 6,64" parsira "0,7" kao dio naziva,
 *    a "6,64" (praćen krajem linije) kao cijenu. ✓
 *
 * 3. KOLUMNA SPLIT: char pozicija 38 unutar content-a (nakon maknutih 5 razmaka
 *    indenta). Lijeva kolumna = [0..38), desna = [38..).
 *
 * 4. PENDING NAME: naziv može biti u liniji IZNAD koda (pending_L/R)
 *    ili ISPOD koda (bare_L/R index u items listi).
 *
 * 5. KATEGORIJA: all-caps linija bez standalone koda (\b\d{3,8}\b)
 *    i bez standalone cijene (\b\d{1,4},\d{2}\b).
 *    Kategorija-headeri tipa "BAČVA (30,50….)" prolaze jer "30,50" nije
 *    praćen word boundary s obje strane.
 */
@Component
public class RotoPdfParser implements PdfParser {

    // Cijena ankorizirana sljedećim kodom ili krajem linije
    private static final Pattern ITEM_RE = Pattern.compile(
            "(\\d{3,8})\\s+(.+?)\\s+(\\d{1,4},\\d{2})(?=\\s+\\d{3,8}|\\s*$)"
    );
    // Artikl bez inline naziva (samo KOD ... CIJENA)
    private static final Pattern BARE_RE = Pattern.compile(
            "(\\d{3,8})\\s+(\\d{1,4},\\d{2})(?=\\s+\\d{3,8}|\\s*$)"
    );

    // Standalone kod i cijena — za čišćenje name fragmenata
    private static final Pattern STANDALONE_CODE  = Pattern.compile("(?<!\\w)\\d{3,8}(?!\\w)");
    private static final Pattern STANDALONE_PRICE = Pattern.compile("(?<!\\w)\\d{1,4},\\d{2}(?!\\w)");

    // Char pozicija split L/R kolumne (unutar content-a, već bez 5-razmačnog indenta)
    private static final int COL_SPLIT = 38;

    @Override
    public List<CatalogueItem> parse(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ValidationException("PDF bytes su prazni");
        }

        List<CatalogueItem> items = new ArrayList<>();

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String fullText = stripper.getText(doc);

            String currentCategory = null;
            String pendingL = "";
            String pendingR = "";
            int bareL = -1;   // index u items[] koji čeka naziv ispod
            int bareR = -1;

            for (String rawLine : fullText.split("\n")) {
                String line    = rawLine.stripTrailing();
                String trimmed = line.strip();

                if (trimmed.isEmpty()) continue;

                // --- Kategorija ---
                if (isCategory(trimmed)) {
                    currentCategory = trimmed.length() > 80 ? trimmed.substring(0, 80) : trimmed;
                    pendingL = pendingR = "";
                    bareL = bareR = -1;
                    continue;
                }

                // --- Item linija (indent=5, šesti znak je digit) ---
                if (isItemLine(line)) {
                    String content = line.length() > 5 ? line.substring(5) : line.strip();

                    String leftPart  = content.length() > COL_SPLIT
                            ? content.substring(0, COL_SPLIT).strip()
                            : content.strip();
                    String rightPart = content.length() > COL_SPLIT
                            ? content.substring(COL_SPLIT).strip()
                            : "";

                    // Lijeva kolumna
                    ParseResult resL = parseItemPart(leftPart, pendingL, items, currentCategory);
                    if (resL != null) {
                        pendingL = "";
                        bareL = resL.bareIndex;
                    }

                    // Desna kolumna
                    ParseResult resR = parseItemPart(rightPart, pendingR, items, currentCategory);
                    if (resR != null) {
                        pendingR = "";
                        bareR = resR.bareIndex;
                    }

                    // Ako nije bilo matcha ni u jednoj kolumni, ne resetiraj pending

                } else {
                    // --- Name-only linija ---
                    String content = line.length() > 5 ? line.substring(5) : trimmed;

                    String leftFrag  = content.length() > COL_SPLIT
                            ? content.substring(0, COL_SPLIT).strip()
                            : content.strip();
                    String rightFrag = content.length() > COL_SPLIT
                            ? content.substring(COL_SPLIT).strip()
                            : "";

                    // Lijeva strana
                    String lf = stripArtefacts(leftFrag);
                    if (!lf.isEmpty()) {
                        if (bareL >= 0 && items.get(bareL).getName().isEmpty()) {
                            items.get(bareL).setName(lf);
                        } else {
                            pendingL = pendingL.isEmpty() ? lf : pendingL + " " + lf;
                        }
                    }

                    // Desna strana
                    String rf = stripArtefacts(rightFrag);
                    if (!rf.isEmpty()) {
                        if (bareR >= 0 && items.get(bareR).getName().isEmpty()) {
                            items.get(bareR).setName(rf);
                        } else {
                            pendingR = pendingR.isEmpty() ? rf : pendingR + " " + rf;
                        }
                    }
                }
            }
        }

        return items;
    }

    // -------------------------------------------------------------------------

    private ParseResult parseItemPart(String part, String pending,
                                      List<CatalogueItem> items, String category) {
        if (part.isEmpty()) return null;

        // Pokušaj ITEM_RE (kod + naziv + cijena)
        Matcher m = ITEM_RE.matcher(part);
        boolean found = false;
        int lastBare = -1;

        while (m.find()) {
            found = true;
            String code  = m.group(1);
            String name  = cleanName(m.group(2));
            String price = m.group(3);

            if (name.isEmpty() && !pending.isEmpty()) {
                name = pending;
            } else if (!name.isEmpty() && !pending.isEmpty()) {
                name = (pending + " " + name).strip();
            }

            CatalogueItem item = new CatalogueItem();
            item.setCode(code);
            item.setName(name);
            item.setPrice(toDecimal(price));
            items.add(item);

            lastBare = name.isEmpty() ? items.size() - 1 : -1;
        }

        if (found) return new ParseResult(lastBare);

        // Pokušaj BARE_RE (kod + cijena, bez naziva)
        Matcher bm = BARE_RE.matcher(part);
        while (bm.find()) {
            found = true;
            String code  = bm.group(1);
            String price = bm.group(2);
            String name  = pending;

            CatalogueItem item = new CatalogueItem();
            item.setCode(code);
            item.setName(name);
            item.setPrice(toDecimal(price));
            items.add(item);

            lastBare = name.isEmpty() ? items.size() - 1 : -1;
        }

        return found ? new ParseResult(lastBare) : null;
    }

    // -------------------------------------------------------------------------

    /** Indent točno 5 razmaka, šesti znak je digit. */
    private boolean isItemLine(String line) {
        return line.length() > 6
                && line.charAt(0) == ' ' && line.charAt(1) == ' '
                && line.charAt(2) == ' ' && line.charAt(3) == ' '
                && line.charAt(4) == ' '
                && Character.isDigit(line.charAt(5));
    }

    /**
     * All-caps linija bez standalone koda i bez standalone cijene.
     * "BAČVA (30,50….)" prođe jer "30,50" nije word-boundary isolated.
     */
    private boolean isCategory(String line) {
        if (line.length() < 3 || line.length() > 100) return false;
        if (STANDALONE_CODE.matcher(line).find())  return false;
        if (STANDALONE_PRICE.matcher(line).find()) return false;
        return line.equals(line.toUpperCase());
    }

    /** Makni ugniježđene kodove iz naziva (artefakt dvo-kolumnog layouta). */
    private String cleanName(String raw) {
        String cleaned = STANDALONE_CODE.matcher(raw).replaceAll("").strip();
        return cleaned.replaceAll("\\s+", " ").strip().replaceAll("^[.,; ]+|[.,; ]+$", "");
    }

    /** Makni i kodove i cijene iz name fragmenta (za name-only linije). */
    private String stripArtefacts(String text) {
        String s = STANDALONE_CODE.matcher(text).replaceAll("");
        s = STANDALONE_PRICE.matcher(s).replaceAll("");
        return s.replaceAll("\\s+", " ").strip().replaceAll("^[.,; ]+|[.,; ]+$", "");
    }

    private BigDecimal toDecimal(String raw) {
        return new BigDecimal(raw.replace(',', '.'));
    }

    // -------------------------------------------------------------------------

    private record ParseResult(int bareIndex) {}
}