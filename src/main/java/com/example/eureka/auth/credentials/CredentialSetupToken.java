package com.example.eureka.auth.credentials;

import java.time.LocalDateTime;

public class CredentialSetupToken {
    private Long id;
    private String token;
    private Long ownerUserId;
    private Long employeeUserId;
    private String ownerUsername;
    private String ownerPlainPassword;
    private String employeeUsername;
    private String employeePlainPassword;
    private boolean used;
    private boolean emailSent;
    private LocalDateTime sendAfter;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public CredentialSetupToken() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Long getEmployeeUserId() {
        return employeeUserId;
    }

    public void setEmployeeUserId(Long employeeUserId) {
        this.employeeUserId = employeeUserId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerPlainPassword() {
        return ownerPlainPassword;
    }

    public void setOwnerPlainPassword(String ownerPlainPassword) {
        this.ownerPlainPassword = ownerPlainPassword;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployeePlainPassword() {
        return employeePlainPassword;
    }

    public void setEmployeePlainPassword(String employeePlainPassword) {
        this.employeePlainPassword = employeePlainPassword;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public LocalDateTime getSendAfter() {
        return sendAfter;
    }

    public void setSendAfter(LocalDateTime sendAfter) {
        this.sendAfter = sendAfter;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
