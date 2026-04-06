package com.example.eureka.catalogue.pdfParser;

import com.example.eureka.catalogue.CatalogueItem;
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

@Component
public class SupplierCataloguePdfParser {

    public List<CatalogueItem> parse(byte[] pdfBytes) throws IOException {
        List<CatalogueItem> items = new ArrayList<>();

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            String[] lines = text.split("\n");

            String currentCategory = null;

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (isCategoryLine(line)) {
                    currentCategory = line;
                    continue;
                }

                CatalogueItem item = tryParseItem(line, currentCategory);
                if (item != null) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    private boolean isCategoryLine(String line) {
        return !line.matches("^\\d{8}.*") && !line.matches(".*\\d+,\\d{2}$")
                && line.length() < 80 && line.equals(line.toUpperCase());
    }

    private CatalogueItem tryParseItem(String line, String category) {
        Pattern pattern = Pattern.compile(
                "^(\\d{8})\\s+(.+?)\\s+(\\d+[,.]\\d{2})$"
        );
        Matcher matcher = pattern.matcher(line.trim());

        if (!matcher.matches()) return null;

        CatalogueItem item = new CatalogueItem();
        item.setCode(matcher.group(1));
        item.setName(cleanName(matcher.group(2)));
        item.setPrice(parsePrice(matcher.group(3)));
        return item;
    }

    private String cleanName(String raw) {
        // Makni barkod iz naziva ako je ostao (8+ znamenki u sredini)
        return raw.replaceAll("\\s+\\d{8,}\\s*", " ").trim();
    }

    private BigDecimal parsePrice(String raw) {
        return new BigDecimal(raw.replace(",", "."));
    }
}