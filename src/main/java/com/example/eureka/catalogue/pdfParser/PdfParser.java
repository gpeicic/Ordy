package com.example.eureka.catalogue.pdfParser;

import com.example.eureka.catalogue.CatalogueItem;

import java.io.IOException;
import java.util.List;

public interface PdfParser {
    List<CatalogueItem> parse(byte[] pdfBytes) throws IOException;
}
