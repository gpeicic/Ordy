package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import com.example.eureka.auth.jwt.JwtService;
import com.example.eureka.auth.jwt.TokenFactory;
import com.example.eureka.auth.jwt.UserValidator;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthServiceImpl implements ApiAuthService {

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
        User user = userValidator.validateCredentials(request.getUsername(), request.getPassword());
        String token = tokenFactory.generateFor(user);
        return new ApiLoginResponse(token);
    }

    @Override
    public ApiRegisterResponse register(ApiRegisterRequest request) {
        User user = registrationService.register(request);
        String token = tokenFactory.generateFor(user);
        return new ApiRegisterResponse(token);
    }

    @Override
    public ApiLoginResponse switchCompany(Long companyId, String token) {
        String username = jwtService.extractUsername(token);
        User user = userMapper.findByUsername(username);

        if (!userCompaniesMapper.existsByUserIdAndCompanyId(user.getId(), companyId)) {
            throw new RuntimeException("User ne pripada toj kompaniji");
        }

        String newToken = tokenFactory.generateFor(user, companyId);
        return new ApiLoginResponse(newToken);
    }
}
