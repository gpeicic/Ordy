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
    @NotBlank(message = "OiB je obavezan")
    private String oib;
    @NotBlank(message = "Adresa je obavezna")
    private String address;
    @NotBlank(message = "Grad je obavezan")
    private String city;
    @NotBlank(message = "Postanski broj je obavezan")
    private String postalCode;

    @NotEmpty(message = "Potreban je najmanje jedan objekt")
    @Valid
    private List<VenueRegisterRequest> venues;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getMerEmail() { return merEmail; }
    public void setMerEmail(String merEmail) { this.merEmail = merEmail; }
    public String getMerPassword() { return merPassword; }
    public void setMerPassword(String merPassword) { this.merPassword = merPassword; }
    public List<VenueRegisterRequest> getVenues() { return venues; }
    public void setVenues(List<VenueRegisterRequest> venues) { this.venues = venues; }
}