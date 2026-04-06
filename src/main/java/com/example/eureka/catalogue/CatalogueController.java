package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> importCatalogue(
            @PathVariable Long supplierId,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fajl je prazan");
        }

        int count = catalogueService.importFromPdf(supplierId, file.getBytes());
        return ResponseEntity.ok("Uvezeno " + count + " artikala");
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<List<CatalogueItem>> getCatalogue(@PathVariable Long supplierId) {
        return ResponseEntity.ok(catalogueService.getBySupplier(supplierId));
    }

    @GetMapping("/search/{supplierId}")
    public ResponseEntity<List<SearchItemForOrderDTO>> fuzzySearch(@PathVariable Long supplierId, @RequestParam String name) {
        return ResponseEntity.ok(catalogueService.fuzzySearchByName(supplierId, name));
    }
}