package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.catalogue.pdfParser.PremiumCataloguePdfParser;
import com.example.eureka.catalogue.pdfParser.RotoPdfParser;
import com.example.eureka.exception.ValidationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    private final CatalogueItemMapper catalogueItemMapper;
    private final PremiumCataloguePdfParser premiumParser;
    private final RotoPdfParser rotoParser;

    public CatalogueServiceImpl(CatalogueItemMapper catalogueItemMapper,
                                PremiumCataloguePdfParser parser, RotoPdfParser rotoParser) {
        this.catalogueItemMapper = catalogueItemMapper;
        this.premiumParser = parser;
        this.rotoParser = rotoParser;
    }

    @CacheEvict(value = "catalogue", key = "#supplierId")
    @Override
    @Transactional
    public int importPremiumFromPdf(Long supplierId, byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ValidationException("PDF je prazan");
        }
        List<CatalogueItem> items = premiumParser.parse(pdfBytes);
        if (items.isEmpty()) {
            throw new ValidationException("Nije moguće parsirati artikle iz PDF-a");
        }
        items.forEach(item -> {
            item.setSupplierId(supplierId);
            catalogueItemMapper.upsert(item);
        });
        return items.size();
    }

    @CacheEvict(value = "catalogue", key = "#supplierId")
    @Override
    @Transactional
    public int importRotoFromPdf(Long supplierId, byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ValidationException("PDF je prazan");
        }
        List<CatalogueItem> items = rotoParser.parse(pdfBytes);
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
    public List<CatalogueItem> getBySupplierPaged(Long supplierId, String search, int page, int size) {
        int offset = page * size;

        if (search == null || search.isBlank()) {
            return catalogueItemMapper.findBySupplierIdPaged(supplierId, size, offset);
        }

        return catalogueItemMapper.searchBySupplierPaged(
                supplierId,
                search == null ? "" : search,
                size,
                offset
        );
    }

    @Override
    public List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Naziv za pretragu je obavezan");
        }

        return catalogueItemMapper.fuzzySearchByName(supplierId, name);
    }

    @CacheEvict(value = "catalogue", key = "#item.supplierId")
    @Override
    @Transactional
    public void upsert(CatalogueItem item) {
        catalogueItemMapper.upsert(item);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        catalogueItemMapper.deleteById(id);
    }

}