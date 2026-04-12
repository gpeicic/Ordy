package com.example.eureka.merAuth;



import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "MerAuth", description = "Autentifikacija i login za mer")
@RequestMapping("/mer")
public class MerAuthController {

    private final MerAuthService merAuthService;

    public MerAuthController(MerAuthService merAuthService) {
        this.merAuthService = merAuthService;
    }

    @PostMapping("/login/{companyId}")
    public ResponseEntity<Void> loginCompany(@PathVariable Long companyId) {
        merAuthService.loginCompany(companyId);
        return ResponseEntity.ok().build();
    }
}