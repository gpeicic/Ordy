package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.springframework.cache.annotation.CacheEvict;

import java.io.IOException;
import java.util.List;

public interface CatalogueService {
    int importPremiumFromPdf(Long supplierId, byte[] pdfBytes) throws IOException;
    int importRotoFromPdf(Long supplierId, byte[] pdfBytes) throws IOException;
    List<CatalogueItem> getBySupplierPaged(Long supplierId, String search, int page, int size);
    List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, String name);
    void upsert(CatalogueItem item);
    void delete(Long id);
}
