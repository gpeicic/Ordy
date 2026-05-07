package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import com.example.eureka.auth.jwt.JwtService;
import com.example.eureka.auth.jwt.TokenFactory;
import com.example.eureka.auth.jwt.UserValidator;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.UnauthorizedException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthServiceImpl implements ApiAuthService {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthServiceImpl.class);

    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final TokenFactory tokenFactory;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserCompaniesMapper userCompaniesMapper;

    public ApiAuthServiceImpl(UserValidator userValidator,
                              RegistrationService registrationService,
                              TokenFactory tokenFactory,
                              JwtService jwtService,
                              UserMapper userMapper,
                              UserCompaniesMapper userCompaniesMapper) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.tokenFactory = tokenFactory;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userCompaniesMapper = userCompaniesMapper;
    }

    @Override
    public ApiLoginResponse login(ApiLoginRequest request) {
        log.info("Login pokušaj za korisnika: {}", request.getUsername());
        User user = userValidator.validateCredentials(request.getUsername(), request.getPassword());
        String token = tokenFactory.generateFor(user);
        log.info("Login uspješan za korisnika: {}", request.getUsername());
        return new ApiLoginResponse(token);
    }

    @Override
    public ApiRegisterResponse register(ApiRegisterRequest request) {
        log.info("Registracija pokušaj");
        User user = registrationService.register(request);
        String token = tokenFactory.generateFor(user);
        log.info("Registracija uspješna, userId: {}", user.getId());
        return new ApiRegisterResponse(token);
    }

    @Override
    public ApiLoginResponse switchCompany(Long companyId, String token) {
        String username = jwtService.extractUsername(token);
        log.info("Switch company pokušaj — korisnik: {}, companyId: {}", username, companyId);
        User user = userMapper.findByUsername(username);

        if (user == null) {
            log.warn("Switch company odbijen — korisnik nije pronađen: {}", username);
            throw new ResourceNotFoundException("User nije pronađen");
        }

        if (!userCompaniesMapper.existsByUserIdAndCompanyId(user.getId(), companyId)) {
            log.warn("Switch company odbijen — korisnik {} ne pripada kompaniji {}", username, companyId);
            throw new UnauthorizedException("User ne pripada toj kompaniji");
        }

        String newToken = tokenFactory.generateFor(user, companyId);
        log.info("Switch company uspješan — korisnik: {}, companyId: {}", username, companyId);
        return new ApiLoginResponse(newToken);
    }
}
