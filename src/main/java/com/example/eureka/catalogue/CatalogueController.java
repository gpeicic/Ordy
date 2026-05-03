package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.exception.ValidationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/catalogue")
@Tag(name = "Catalogue", description = "Upravljanje cjenikom")
public class CatalogueController {

    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @GetMapping("/{supplierId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CatalogueItem>> getBySupplier(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(
                catalogueService.getBySupplierPaged(supplierId, search, page, size)
        );
    }

    @PostMapping(value = "/premium/{supplierId}/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importPremiumPdf(
            @PathVariable Long supplierId,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new ValidationException("PDF file je prazan");
        }

        int importedCount = catalogueService.importPremiumFromPdf(
                supplierId,
                file.getBytes()
        );

        return ResponseEntity.ok(
                "Premium katalog uspješno importan. Broj artikala: " + importedCount
        );
    }

    @PostMapping(value = "/roto/{supplierId}/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importRotoPdf(
            @PathVariable Long supplierId,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new ValidationException("PDF file je prazan");
        }

        int importedCount = catalogueService.importRotoFromPdf(
                supplierId,
                file.getBytes()
        );

        return ResponseEntity.ok(
                "Roto katalog uspješno importan. Broj artikala: " + importedCount
        );
    }

    @GetMapping("/{supplierId}/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SearchItemForOrderDTO>> search(
            @PathVariable Long supplierId,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(catalogueService.fuzzySearchByName(supplierId, name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> create(@RequestBody CatalogueItem item) {
        catalogueService.upsert(item);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody CatalogueItem item) {
        item.setId(id);
        catalogueService.upsert(item);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        catalogueService.delete(id);
        return ResponseEntity.ok().build();
    }
}