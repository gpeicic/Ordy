package com.example.eureka.venue;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Venue", description = "Pristup ugostiteljskim objektima")
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/{id}")
    public Venue getById(@PathVariable Long id) {
        return venueService.getById(id);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Venue>> getByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(venueService.getByCompanyId(companyId));
    }

    @PostMapping
    public ResponseEntity<Venue> create(@RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.create(venue));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> update(@PathVariable Long id, @RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.update(id, venue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}