package com.example.eureka.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CompanyRegisterRequest {
    @NotBlank(message = "Naziv kompanije je obavezan")
    private String companyName;

    @Email(message = "Neispravan email format")
    @NotBlank(message = "MER email je obavezan")
    private String merEmail;

    @NotBlank(message = "MER lozinka je obavezna")
    private String merPassword;

    @NotEmpty(message = "Potreban je najmanje jedan objekt")
    @Valid
    private List<VenueRegisterRequest> venues;



    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getMerEmail() { return merEmail; }
    public void setMerEmail(String merEmail) { this.merEmail = merEmail; }
    public String getMerPassword() { return merPassword; }
    public void setMerPassword(String merPassword) { this.merPassword = merPassword; }
    public List<VenueRegisterRequest> getVenues() { return venues; }
    public void setVenues(List<VenueRegisterRequest> venues) { this.venues = venues; }
}