package com.example.eureka.auth.dto;

import java.util.List;

public class ApiRegisterRequest {
    private String username;
    private String password;
    private List<CompanyRegisterRequest> companies;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<CompanyRegisterRequest> getCompanies() { return companies; }
    public void setCompanies(List<CompanyRegisterRequest> companies) { this.companies = companies; }
}