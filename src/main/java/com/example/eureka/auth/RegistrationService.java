package com.example.eureka.auth;

import com.example.eureka.auth.credentials.CredentialSetupToken;
import com.example.eureka.auth.credentials.CredentialSetupTokenMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789!@#$";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final TextEncryptor encryptor;
    private final Long PROVIDER_ROLE_ID = 5L;
    private final Long USER_ROLE_ID = 2L;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final UserCompaniesMapper userCompaniesMapper;
    private final VenueMapper venueMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final CredentialSetupTokenMapper setupTokenMapper;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public RegistrationService(UserMapper userMapper, CompanyMapper companyMapper,
                               UserCompaniesMapper userCompaniesMapper, VenueMapper venueMapper,
                               PasswordEncoder passwordEncoder,
                               ApplicationEventPublisher eventPublisher,
                               TextEncryptor encryptor,
                               CredentialSetupTokenMapper setupTokenMapper) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.userCompaniesMapper = userCompaniesMapper;
        this.venueMapper = venueMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.encryptor = encryptor;
        this.setupTokenMapper = setupTokenMapper;
    }

    @Transactional
    public User register(ApiRegisterRequest request) {
        String ownerUsername    = generateUsername("vlasnik");
        String ownerPassword    = generatePassword();
        String employeeUsername = generateUsername("zaposlenik");
        String employeePassword = generatePassword();

        log.info("Registracija — kreiranje usera: {}", ownerUsername);
        User owner = createUser(ownerUsername, ownerPassword, request.getEmail());
        log.info("Owner kreiran — userId: {}, username: {}", owner.getId(), owner.getUsername());

        User employee = createEmployeeUser(employeeUsername, employeePassword, request.getEmail());
        log.info("Employee kreiran — userId: {}, username: {}", employee.getId(), employee.getUsername());

        for (CompanyRegisterRequest companyReq : request.getCompanies()) {
            Company company = createCompany(companyReq);
            userCompaniesMapper.insertUserCompany(owner.getId(), company.getId());
            userCompaniesMapper.insertUserCompany(employee.getId(), company.getId());
            log.info("Company kreirana — companyId: {}, name: {}", company.getId(), company.getName());
            createVenues(companyReq.getVenues(), company.getId(), owner.getId());
            eventPublisher.publishEvent(new CompanyRegisteredEvent(company.getId()));
        }

        // Kreiraj setup token — email se šalje tek za 24h (scheduler)
        String tokenValue = UUID.randomUUID().toString();
        CredentialSetupToken setupToken = new CredentialSetupToken();
        setupToken.setToken(tokenValue);
        setupToken.setOwnerUserId(owner.getId());
        setupToken.setEmployeeUserId(employee.getId());
        setupToken.setOwnerUsername(ownerUsername);
        setupToken.setOwnerPlainPassword(ownerPassword);
        setupToken.setEmployeeUsername(employeeUsername);
        setupToken.setEmployeePlainPassword(employeePassword);
        setupToken.setSendAfter(LocalDateTime.now().plusHours(1));
        setupToken.setExpiresAt(LocalDateTime.now().plusHours(2));
        setupTokenMapper.insert(setupToken);

        log.info("Registracija završena. Email s kredencijalima šalje se za 24h na: {}", request.getEmail());

        return owner;
    }

    // ─── helpers ────────────────────────────────────────────────────────────

    private User createUser(String username, String rawPassword, String email) {
        User owner = new User();
        owner.setUsername(username);
        owner.setPassword(passwordEncoder.encode(rawPassword));
        owner.setEmail(email);
        owner.setRole_id(PROVIDER_ROLE_ID);
        userMapper.insert(owner);
        return owner;
    }

    private User createEmployeeUser(String username, String rawPassword, String email) {
        User employee = new User();
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(rawPassword));
        employee.setEmail(email);
        employee.setRole_id(USER_ROLE_ID);
        userMapper.insert(employee);
        return employee;
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
            log.info("Venue kreiran — venueId: {}, name: {}", venue.getId(), venue.getName());
        }
    }

    private String generateUsername(String prefix) {
        return prefix + "_" + (1000 + RANDOM.nextInt(9000));
    }

    private String generatePassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}