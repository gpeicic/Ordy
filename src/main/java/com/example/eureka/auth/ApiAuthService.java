package com.example.eureka.auth;

public interface ApiAuthService {
    ApiLoginResponse login(ApiLoginRequest request);
    ApiRegisterResponse register(ApiRegisterRequest request);
}
