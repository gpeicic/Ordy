package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class ApiAuthController {

    private final ApiAuthService authService;

    public ApiAuthController(ApiAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiLoginResponse login(@RequestBody ApiLoginRequest request) {

        return authService.login(request);
    }

    @PostMapping("/register")
    public ApiRegisterResponse register(@RequestBody ApiRegisterRequest request) {
        return authService.register(request);
    }
}