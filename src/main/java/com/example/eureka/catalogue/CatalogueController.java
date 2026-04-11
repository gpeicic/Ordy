package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

    private final CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @PostMapping("/import/{supplierId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> importCatalogue(
            @PathVariable Long supplierId,
            @RequestParam("file") MultipartFile file) throws IOException {
        int count = catalogueService.importFromPdf(supplierId, file.getBytes());
        return ResponseEntity.ok("Uvezeno " + count + " artikala");
    }

    @GetMapping("/{supplierId}")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CatalogueItem>> getCatalogue(@PathVariable Long supplierId) {
        return ResponseEntity.ok(catalogueService.getBySupplier(supplierId));
    }

    @GetMapping("/search/{supplierId}")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<SearchItemForOrderDTO>> fuzzySearch(@PathVariable Long supplierId, @RequestParam String name) {
        return ResponseEntity.ok(catalogueService.fuzzySearchByName(supplierId, name));
    }
}