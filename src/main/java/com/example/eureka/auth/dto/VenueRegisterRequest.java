package com.example.eureka.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class VenueRegisterRequest {
    @NotBlank(message = "Naziv objekta je obavezan")
    private String name;

    @NotBlank(message = "Adresa je obavezna")
    private String address;

    @NotBlank(message = "Grad je obavezan")
    private String city;

    @NotBlank(message = "Poštanski broj je obavezan")
    private String postalCode;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}