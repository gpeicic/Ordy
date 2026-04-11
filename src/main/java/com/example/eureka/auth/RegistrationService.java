package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.CompanyRegisterRequest;
import com.example.eureka.auth.dto.VenueRegisterRequest;
import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import com.example.eureka.venue.Venue;
import com.example.eureka.venue.VenueMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class RegistrationService {
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final UserCompaniesMapper userCompaniesMapper;
    private final VenueMapper venueMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserMapper userMapper, CompanyMapper companyMapper,
                               UserCompaniesMapper userCompaniesMapper, VenueMapper venueMapper,
                               PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.userCompaniesMapper = userCompaniesMapper;
        this.venueMapper = venueMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(ApiRegisterRequest request) {
        validateRegisterRequest(request);

        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new ValidationException("Korisničko ime već postoji");
        }

        User user = createUser(request);
        for (CompanyRegisterRequest companyReq : request.getCompanies()) {
            Company company = createCompany(companyReq);
            userCompaniesMapper.insertUserCompany(user.getId(), company.getId());
            createVenues(companyReq.getVenues(), company.getId(), user.getId());
        }
        return user;
    }

    private User createUser(ApiRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole_id(2L);
        userMapper.insert(user);
        return user;
    }

    private Company createCompany(CompanyRegisterRequest request) {
        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setMerEmail(request.getMerEmail());
        company.setMerPassword(request.getMerPassword());
        companyMapper.insert(company);
        return company;
    }

    private void createVenues(List<VenueRegisterRequest> venues, Long companyId, Long userId) {
        for (VenueRegisterRequest venueReq : venues) {
            Venue venue = new Venue();
            venue.setName(venueReq.getName());
            venue.setAddress(venueReq.getAddress());
            venue.setCity(venueReq.getCity());
            venue.setPostalCode(venueReq.getPostalCode());
            venue.setCompanyId(companyId);
            venue.setUserId(userId);
            venueMapper.insert(venue);
        }
    }
    private void validateRegisterRequest(ApiRegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new ValidationException("Korisničko ime je obavezno");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new ValidationException("Lozinka mora imati najmanje 8 znakova");
        }
        if (request.getCompanies() == null || request.getCompanies().isEmpty()) {
            throw new ValidationException("Potrebna je najmanje jedna kompanija");
        }
        for (CompanyRegisterRequest company : request.getCompanies()) {
            if (company.getMerEmail() == null || !company.getMerEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
                throw new ValidationException("Neispravan email format: " + company.getMerEmail());
            }
            if (company.getVenues() == null || company.getVenues().isEmpty()) {
                throw new ValidationException("Kompanija mora imati najmanje jedan objekt");
            }
        }
    }
}