package com.example.eureka.company;

import com.example.eureka.company.dto.CompanyResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CompanyResponseDTO>> findCompaniesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(companyService.findCompaniesByUserId(userId));
    }
}
