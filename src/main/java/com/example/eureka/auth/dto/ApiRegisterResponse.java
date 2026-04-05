package com.example.eureka.auth.dto;

public class ApiRegisterResponse {

    private String accessToken;

    public ApiRegisterResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}