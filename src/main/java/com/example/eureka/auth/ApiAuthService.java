package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiLoginRequest;
import com.example.eureka.auth.dto.ApiLoginResponse;
import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.auth.dto.ApiRegisterResponse;

public interface ApiAuthService {
    ApiLoginResponse login(ApiLoginRequest request);
    ApiRegisterResponse register(ApiRegisterRequest request);
}
