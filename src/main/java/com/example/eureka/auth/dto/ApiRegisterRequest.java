package com.example.eureka.auth.dto;

public class ApiRegisterRequest {

    private String username;
    private String password;
    private String companyName;
    private String merEmail;
    private String merPassword;
    private String address;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getUsername() {
        return username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMerEmail() {
        return merEmail;
    }

    public void setMerEmail(String merEmail) {
        this.merEmail = merEmail;
    }

    public String getMerPassword() {
        return merPassword;
    }

    public void setMerPassword(String merPassword) {
        this.merPassword = merPassword;
    }
}
