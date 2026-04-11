package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import com.example.eureka.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ApiAuthController {

    private final ApiAuthService authService;

    public ApiAuthController(ApiAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiLoginResponse> login(@Valid @RequestBody ApiLoginRequest request) {
       return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiRegisterResponse> register(@Valid @RequestBody ApiRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/switch-company/{companyId}")
    public ResponseEntity<ApiLoginResponse> switchCompany(@PathVariable Long companyId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Neispravan token");
        }
        String token = authHeader.substring(7);
        return ResponseEntity.ok(authService.switchCompany(companyId, token));
    }
}