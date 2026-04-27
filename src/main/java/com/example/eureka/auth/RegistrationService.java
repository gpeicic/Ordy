package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.CompanyRegisterRequest;
import com.example.eureka.auth.dto.VenueRegisterRequest;
import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.merInvoices.CompanyRegisteredEvent;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import com.example.eureka.venue.Venue;
import com.example.eureka.venue.VenueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Component
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    private final TextEncryptor encryptor;
    private final Long DEFAULT_ROLE_ID = 2L;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final UserCompaniesMapper userCompaniesMapper;
    private final VenueMapper venueMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public RegistrationService(UserMapper userMapper, CompanyMapper companyMapper,
                               UserCompaniesMapper userCompaniesMapper, VenueMapper venueMapper,
                               PasswordEncoder passwordEncoder,
                               ApplicationEventPublisher eventPublisher, TextEncryptor encryptor) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.userCompaniesMapper = userCompaniesMapper;
        this.venueMapper = venueMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.encryptor = encryptor;
    }

    @Transactional
    public User register(ApiRegisterRequest request) {

        log.info("Registracija — kreiranje usera: {}", request.getUsername());
        User user = createUser(request);
        log.info("User kreiran — userId: {}, username: {}", user.getId(), user.getUsername());

        for (CompanyRegisterRequest companyReq : request.getCompanies()) {
            Company company = createCompany(companyReq);
            userCompaniesMapper.insertUserCompany(user.getId(), company.getId());
            log.info("Company kreirana — companyId: {}, name: {}, userId: {}", company.getId(), company.getName(), user.getId());
            createVenues(companyReq.getVenues(), company.getId(), user.getId());

            eventPublisher.publishEvent(new CompanyRegisteredEvent(company.getId()));
        }
        return user;
    }

    private User createUser(ApiRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole_id(DEFAULT_ROLE_ID);
        userMapper.insert(user);
        return user;
    }

    private Company createCompany(CompanyRegisterRequest request) {
        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setMerEmail(request.getMerEmail());
        company.setMerPassword(encryptor.encrypt(request.getMerPassword()));
        company.setOib(request.getOib());
        company.setAddress(request.getAddress());
        company.setCity(request.getCity());
        company.setPostalCode(request.getPostalCode());
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
            log.info("Venue kreiran — venueId: {}, name: {}, companyId: {}", venue.getId(), venue.getName(), companyId);
        }
    }

}