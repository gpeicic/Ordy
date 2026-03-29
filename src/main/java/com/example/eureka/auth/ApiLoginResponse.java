package com.example.eureka.auth;

public class ApiLoginResponse {
    private String accessToken;

    public ApiLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
