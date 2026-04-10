package com.example.eureka.venue;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueMapper venueMapper;

    public VenueServiceImpl(VenueMapper venueMapper) {
        this.venueMapper = venueMapper;
    }

    @Override
    public Venue getById(Long id) {
        return venueMapper.findById(id);
    }

    @Override
    public List<Venue> getByCompanyId(Long companyId) {
        return venueMapper.findByCompanyId(companyId);
    }

    @Override
    public Venue create(Venue venue) {
        venueMapper.insert(venue);
        return venue;
    }

    @Override
    public Venue update(Long id, Venue venue) {
        venue.setId(id);
        venueMapper.update(venue);
        return venue;
    }

    @Override
    public void delete(Long id) {
        venueMapper.delete(id);
    }
}