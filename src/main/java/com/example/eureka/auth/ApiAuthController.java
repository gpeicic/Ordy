package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/switch-company/{companyId}")
    public ApiLoginResponse switchCompany(@PathVariable Long companyId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return authService.switchCompany(companyId, token);
    }
}