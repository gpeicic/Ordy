package com.example.eureka.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ApiRegisterRequest {
    @Email(message = "Neispravan email format")
    @NotBlank(message = "Email je obavezan")
    private String email;

    @NotEmpty(message = "Potrebna je najmanje jedna kompanija")
    @Valid
    private List<CompanyRegisterRequest> companies;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CompanyRegisterRequest> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyRegisterRequest> companies) {
        this.companies = companies;
    }
}