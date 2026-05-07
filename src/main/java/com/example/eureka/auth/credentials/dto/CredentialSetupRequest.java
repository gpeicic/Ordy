package com.example.eureka.auth.credentials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CredentialSetupRequest {
    @NotBlank(message = "Token je obavezan")
    private String token;

    @NotBlank(message = "Korisničko ime vlasnika je obavezno")
    private String ownerUsername;

    @Size(min = 8, message = "Lozinka vlasnika mora imati najmanje 8 znakova")
    @NotBlank(message = "Lozinka vlasnika je obavezna")
    private String ownerPassword;

    @NotBlank(message = "Korisničko ime zaposlenika je obavezno")
    private String employeeUsername;

    @Size(min = 8, message = "Lozinka zaposlenika mora imati najmanje 8 znakova")
    @NotBlank(message = "Lozinka zaposlenika je obavezna")
    private String employeePassword;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public String getOwnerPassword() { return ownerPassword; }
    public void setOwnerPassword(String ownerPassword) { this.ownerPassword = ownerPassword; }
    public String getEmployeeUsername() { return employeeUsername; }
    public void setEmployeeUsername(String employeeUsername) { this.employeeUsername = employeeUsername; }
    public String getEmployeePassword() { return employeePassword; }
    public void setEmployeePassword(String employeePassword) { this.employeePassword = employeePassword; }
}
