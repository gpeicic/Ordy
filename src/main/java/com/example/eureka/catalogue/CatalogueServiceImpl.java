package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.catalogue.pdfParser.SupplierCataloguePdfParser;
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
        List<CatalogueItem> items = parser.parse(pdfBytes);
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
        Long millis = System.currentTimeMillis();
        List<SearchItemForOrderDTO> result = catalogueItemMapper.fuzzySearchByName(supplierId, name);
        System.out.println("Time: " + (System.currentTimeMillis() - millis));
        return catalogueItemMapper.fuzzySearchByName(supplierId, name);
    }
}