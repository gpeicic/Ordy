package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.springframework.cache.annotation.CacheEvict;

import java.io.IOException;
import java.util.List;

public interface CatalogueService {
    int importFromPdf(Long supplierId, byte[] pdfBytes) throws IOException;
    List<CatalogueItem> getBySupplier(Long supplierId);
    List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, String name);
    @CacheEvict(value = "catalogue", key = "#item.supplierId")
    void upsert(CatalogueItem item);
    void delete(Long id);
}
