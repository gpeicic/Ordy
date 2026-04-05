package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import com.example.eureka.auth.jwt.TokenFactory;
import com.example.eureka.auth.jwt.UserValidator;
import com.example.eureka.user.User;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthServiceImpl implements ApiAuthService {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final TokenFactory tokenFactory;

    public ApiAuthServiceImpl(UserValidator userValidator,
                              RegistrationService registrationService,
                              TokenFactory tokenFactory) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.tokenFactory = tokenFactory;
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
}
