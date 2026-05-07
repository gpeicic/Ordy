package com.example.eureka.auth.credentials.email;

public interface EmailService {
    void sendCredentials(String toEmail,
                         String ownerUsername, String ownerPassword,
                         String employeeUsername, String employeePassword,
                         String setupLink);
}
