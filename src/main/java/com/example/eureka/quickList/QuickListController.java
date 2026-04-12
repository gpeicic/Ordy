package com.example.eureka.quickList;

import com.example.eureka.quickList.dto.QuickListItemDetail;
import com.example.eureka.quickList.dto.QuickListRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quickLists")
public class QuickListController {

    private final QuickListService quickListService;

    public QuickListController(QuickListService quickListService) {
        this.quickListService = quickListService;
    }

    @PostMapping
    public ResponseEntity<QuickList> create(@RequestBody QuickListRequest request) {
        QuickList quickList = new QuickList();
        quickList.setName(request.getName());
        quickList.setCompanyId(request.getCompanyId());
        quickList.setUserId(request.getUserId());
        quickList.setSupplierId(request.getSupplierId());

        List<QuickListItem> items = request.getItems().stream().map(i -> {
            QuickListItem item = new QuickListItem();
            item.setCatalogueItemId(i.getCatalogueItemId());
            item.setQuantity(i.getQuantity());
            return item;
        }).toList();

        return ResponseEntity.ok(quickListService.create(quickList, items));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<QuickList>> getByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(quickListService.getByCompanyId(companyId));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<QuickListItemDetail>> getItems(@PathVariable Long id) {
        return ResponseEntity.ok(quickListService.getItems(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quickListService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuickList> update(@PathVariable Long id, @RequestBody String name) {
        return ResponseEntity.ok(quickListService.update(id, name));
    }
}