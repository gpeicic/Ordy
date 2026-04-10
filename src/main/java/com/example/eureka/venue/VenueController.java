package com.example.eureka.venue;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public List<Venue> getByCompanyId(@PathVariable Long companyId) {
        return venueService.getByCompanyId(companyId);
    }

    @PostMapping
    public Venue create(@RequestBody Venue venue) {
        return venueService.create(venue);
    }

    @PutMapping("/{id}")
    public Venue update(@PathVariable Long id, @RequestBody Venue venue) {
        return venueService.update(id, venue);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        venueService.delete(id);
    }
}