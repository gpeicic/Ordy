package com.example.eureka.auth.credentials;

import com.example.eureka.auth.credentials.dto.CredentialSetupRequest;

public interface CredentialSetupService {
    void setupCredentials(CredentialSetupRequest request);
}
