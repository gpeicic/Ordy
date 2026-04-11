package com.example.eureka.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ApiRegisterRequest {
    @NotBlank(message = "Korisničko ime je obavezno")
    private String username;

    @Size(min = 8, message = "Lozinka mora imati najmanje 8 znakova")
    @NotBlank(message = "Lozinka je obavezna")
    private String password;

    @NotEmpty(message = "Potrebna je najmanje jedna kompanija")
    @Valid
    private List<CompanyRegisterRequest> companies;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<CompanyRegisterRequest> getCompanies() { return companies; }
    public void setCompanies(List<CompanyRegisterRequest> companies) { this.companies = companies; }
}