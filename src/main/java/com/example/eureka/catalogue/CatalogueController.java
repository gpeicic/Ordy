package com.example.eureka.catalogue;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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