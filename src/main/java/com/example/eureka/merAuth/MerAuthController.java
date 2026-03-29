package com.example.eureka.merAuth;



import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mer")
public class MerAuthController {

    private final MerAuthService merAuthService;

    public MerAuthController(MerAuthService merAuthService) {
        this.merAuthService = merAuthService;
    }

    @PostMapping("/login/{companyId}")
    public void loginCompany(@PathVariable Long companyId) {
        merAuthService.loginCompany(companyId);
    }
}