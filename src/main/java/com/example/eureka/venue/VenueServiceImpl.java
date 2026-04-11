package com.example.eureka.venue;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
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
        if (id == null) {
            throw new ValidationException("id je obavezan");
        }
        Venue venue = venueMapper.findById(id);
        if (venue == null) {
            throw new ResourceNotFoundException("Venue nije pronađen: " + id);
        }
        return venue;
    }

    @Override
    public List<Venue> getByCompanyId(Long companyId) {
        if (companyId == null) {
            throw new ValidationException("companyId je obavezan");
        }
        return venueMapper.findByCompanyId(companyId);
    }

    @Override
    public Venue create(Venue venue) {
        if (venue == null) {
            throw new ValidationException("Venue je null");
        }
        if (venue.getCompanyId() == null || venue.getUserId() == null) {
            throw new ValidationException("companyId i userId su obavezni");
        }
        venueMapper.insert(venue);
        return venue;
    }

    @Override
    public Venue update(Long id, Venue venue) {
        if (id == null) {
            throw new ValidationException("id je obavezan");
        }
        Venue existing = venueMapper.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Venue nije pronađen: " + id);
        }
        venue.setId(id);
        venueMapper.update(venue);
        return venue;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("id je obavezan");
        }
        Venue existing = venueMapper.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Venue nije pronađen: " + id);
        }
        venueMapper.delete(id);
    }
}