package com.example.eureka.company;

public class Company {
    private Long id;
    private String name;
    private String merEmail;
    private String merPassword;

    public Company() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
