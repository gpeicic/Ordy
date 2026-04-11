package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.catalogue.pdfParser.SupplierCataloguePdfParser;
import com.example.eureka.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    private final CatalogueItemMapper catalogueItemMapper;
    private final SupplierCataloguePdfParser parser;

    public CatalogueServiceImpl(CatalogueItemMapper catalogueItemMapper,
                                SupplierCataloguePdfParser parser) {
        this.catalogueItemMapper = catalogueItemMapper;
        this.parser = parser;
    }

    @Override
    @Transactional
    public int importFromPdf(Long supplierId, byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ValidationException("PDF je prazan");
        }
        List<CatalogueItem> items = parser.parse(pdfBytes);
        if (items.isEmpty()) {
            throw new ValidationException("Nije moguće parsirati artikle iz PDF-a");
        }
        items.forEach(item -> {
            item.setSupplierId(supplierId);
            catalogueItemMapper.upsert(item);
        });
        return items.size();
    }

    @Override
    public List<CatalogueItem> getBySupplier(Long supplierId) {
        return catalogueItemMapper.findBySupplierId(supplierId);
    }

    @Override
    public List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Naziv za pretragu je obavezan");
        }
        return catalogueItemMapper.fuzzySearchByName(supplierId, name);
    }
}