package com.example.eureka.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{userId}")
    public Iterable<Company> findCompaniesByUserId(Long userId) {
        return companyService.findCompaniesByUserId(userId);
    }
}
