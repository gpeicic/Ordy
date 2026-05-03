package com.example.eureka.catalogue.pdfParser;

import com.example.eureka.catalogue.CatalogueItem;
import com.example.eureka.exception.ValidationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser za Roto cjenik PDF (dvo-kolumni layout, A4).
 *
 * PDF struktura:
 *  - Svaka stranica ima dvije kolumne (lijeva / desna).
 *  - Svaki artikl: KOD (3-8 znamenki) ... NAZIV ... CIJENA (format X,XX).
 *  - Naziv može biti u redu iznad ili ispod koda — parser parsira
 *    svaku kolumnu zasebno, liniju po liniju, i prati pending naziv.
 *  - Kategorija: all-caps red bez kodova i cijena.
 *  - Footer (y > 790pt) se ignorira.
 */
@Component
public class RotoPdfParser implements PdfParser {

    // PDF koordinate (pt), izmjerene iz layouta — A4 = 595 x 842 pt
    private static final Rectangle2D LEFT_COL  = new Rectangle2D.Float(28,  60, 258, 730);
    private static final Rectangle2D RIGHT_COL = new Rectangle2D.Float(286, 60, 260, 730);

    // KOD: 3-8 znamenki na početku tokena
    private static final Pattern CODE_RE  = Pattern.compile("^\\d{3,8}$");
    // CIJENA: decimalni broj s dvije decimale (zarez)
    private static final Pattern PRICE_RE = Pattern.compile("^\\d{1,4},\\d{2}$");

    // Linija-item: CODE ... PRICE (price je zadnji PRICE_RE token u liniji)
    // Koristimo za parsiranje jedne kolumne-linije.
    private static final Pattern LINE_RE  = Pattern.compile(
            "(\\d{3,8})\\s+(.*?)\\s+(\\d{1,4},\\d{2})\\s*$"
    );
    @Override
    public List<CatalogueItem> parse(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ValidationException("PDF bytes su prazni");
        }

        List<CatalogueItem> items = new ArrayList<>();

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            for (int pageIdx = 0; pageIdx < doc.getNumberOfPages(); pageIdx++) {
                PDPage page = doc.getPage(pageIdx);
                String leftText  = extractColumn(doc, page, LEFT_COL);
                String rightText = extractColumn(doc, page, RIGHT_COL);

                String[] leftLines  = leftText.split("\\n");
                String[] rightLines = rightText.split("\\n");

                // Parsiramo svaku kolumnu zasebno; kategorija se dijeli između kolumni
                // ali je uvijek u lijevoj kolumni (header ima x < 250)
                String[] categoryHolder = { null };

                parseColumn(leftLines,  categoryHolder, items);
                parseColumn(rightLines, categoryHolder, items);
            }
        }

        return items;
    }

    private String extractColumn(PDDocument doc, PDPage page, Rectangle2D region) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        stripper.addRegion("col", region);
        stripper.extractRegions(page);
        return stripper.getTextForRegion("col");
    }

    /**
     * Parsira linije jedne kolumne.
     *
     * Logika:
     * - All-caps linija bez koda/cijene = kategorija (ažurira categoryHolder)
     * - Linija s kodom+cijenom = artikl; inline naziv je između koda i cijene
     * - Linija bez koda/cijene = pendingName (naziv koji je iznad koda)
     * - Linija bez inline naziva + postoji pendingName => koristi pending
     * - Linija bez inline naziva + nema pending => sljedeća ne-anchor linija
     *   je "nastavak" (naziv ispod koda) — hvatamo je ako je item prethodan
     */
    private void parseColumn(String[] lines, String[] categoryHolder, List<CatalogueItem> items) {
        String pendingName = null;
        CatalogueItem pendingItem = null;  // item koji čeka naziv ispod

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            // Pokušaj parsirati kao item liniju
            ItemLine parsed = tryParseItemLine(line);

            if (parsed == null) {
                // Nije item linija
                if (isCategory(line)) {
                    categoryHolder[0] = line;
                    pendingName = null;
                    pendingItem = null;
                } else {
                    // Može biti naziv iznad sljedećeg koda (pending)
                    // ili nastavak naziva ispod prethodnog koda
                    String nameFrag = extractNameFragment(line);
                    if (!nameFrag.isEmpty()) {
                        if (pendingItem != null && (pendingItem.getName() == null || pendingItem.getName().isEmpty())) {
                            // Naziv ispod koda — dopuni prethodni item
                            pendingItem.setName(nameFrag);
                            pendingItem = null;
                        } else {
                            // Naziv iznad sljedećeg koda
                            pendingName = pendingName == null
                                    ? nameFrag
                                    : pendingName + " " + nameFrag;
                            pendingItem = null;
                        }
                    }
                }
                continue;
            }

            // Je item linija
            String name = parsed.inlineName;

            if (name.isEmpty() && pendingName != null) {
                name = pendingName;
            }

            pendingName = null;

            CatalogueItem item = new CatalogueItem();
            item.setCode(parsed.code);
            item.setName(name);
            item.setPrice(parsePrice(parsed.price));

            items.add(item);

            // Ako i dalje nema naziva, možda će doći u sljedećoj liniji
            pendingItem = name.isEmpty() ? item : null;
        }
    }

    /**
     * Pokušava parsirati liniju kao "KOD [naziv] CIJENA".
     * Cijena mora biti zadnji token koji matchira PRICE_RE.
     * Kod mora biti prvi token koji matchira CODE_RE, lijevo od cijene.
     */
    private ItemLine tryParseItemLine(String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length < 2) return null;

        // Nađi zadnji price token
        int priceIdx = -1;
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (PRICE_RE.matcher(tokens[i]).matches()) {
                priceIdx = i;
                break;
            }
        }
        if (priceIdx < 0) return null;

        // Nađi zadnji code token lijevo od cijene
        int codeIdx = -1;
        for (int i = priceIdx - 1; i >= 0; i--) {
            if (CODE_RE.matcher(tokens[i]).matches()) {
                codeIdx = i;
                break;
            }
        }
        if (codeIdx < 0) return null;

        String code  = tokens[codeIdx];
        String price = tokens[priceIdx];

        // Inline naziv: tokeni između koda i cijene, bez ugniježđenih cijena i kodova
        StringBuilder nameSb = new StringBuilder();
        for (int i = codeIdx + 1; i < priceIdx; i++) {
            String t = tokens[i];
            if (PRICE_RE.matcher(t).matches()) continue;  // ukloni volumene tipa 30,00
            if (CODE_RE.matcher(t).matches())  continue;  // ukloni strane kodove
            if (!nameSb.isEmpty()) nameSb.append(' ');
            nameSb.append(t);
        }

        return new ItemLine(code, nameSb.toString().trim(), price);
    }

    /**
     * All-caps linija bez kodova i cijena = kategorija.
     */
    private boolean isCategory(String line) {
        if (!line.equals(line.toUpperCase())) return false;
        if (line.length() < 3) return false;
        for (String token : line.split("\\s+")) {
            if (PRICE_RE.matcher(token).matches()) return false;
            if (CODE_RE.matcher(token).matches())  return false;
        }
        return true;
    }

    /**
     * Izvuci tekst koji nije kod ni cijena (potencijalni naziv fragment).
     */
    private String extractNameFragment(String line) {
        StringBuilder sb = new StringBuilder();
        for (String token : line.split("\\s+")) {
            if (PRICE_RE.matcher(token).matches()) continue;
            if (CODE_RE.matcher(token).matches())  continue;
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(token);
        }
        return sb.toString().trim();
    }

    private BigDecimal parsePrice(String raw) {
        return new BigDecimal(raw.replace(',', '.'));
    }

    private record ItemLine(String code, String inlineName, String price) {}
}