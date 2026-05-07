package com.example.eureka.auth;

import com.example.eureka.auth.credentials.CredentialSetupService;
import com.example.eureka.auth.credentials.dto.CredentialSetupRequest;
import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;
import com.example.eureka.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Auth", description = "Autentifikacija i registracija")
@RequestMapping("/auth")
public class ApiAuthController {

    private final ApiAuthService authService;
    private final CredentialSetupService setupService;
    public ApiAuthController(ApiAuthService authService,CredentialSetupService setupService) {
        this.authService = authService;
        this.setupService = setupService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiLoginResponse> login(@Valid @RequestBody ApiLoginRequest request) {
       return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiRegisterResponse> register(@Valid @RequestBody ApiRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/setup-credentials")
    public ResponseEntity<String> setup(@Valid @RequestBody CredentialSetupRequest request) {
        setupService.setupCredentials(request);
        return ResponseEntity.ok("Korisničko ime i lozinka uspješno promijenjeni. Možete se prijaviti.");
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