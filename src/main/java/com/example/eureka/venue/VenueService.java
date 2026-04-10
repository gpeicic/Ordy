package com.example.eureka.venue;

import java.util.List;

public interface VenueService {
    Venue getById(Long id);
    List<Venue> getByCompanyId(Long companyId);
    Venue create(Venue venue);
    Venue update(Long id, Venue venue);
    void delete(Long id);
}