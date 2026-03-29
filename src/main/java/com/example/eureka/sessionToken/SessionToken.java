package com.example.eureka.sessionToken;

import java.time.LocalDateTime;

public class SessionToken {
    private Long id;
    private Long companyId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiration;

    public SessionToken() {}
    public SessionToken(Long id, Long companyId, String accessToken, String refreshToken, LocalDateTime expiration) {
        this.id = id;
        this.companyId = companyId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
