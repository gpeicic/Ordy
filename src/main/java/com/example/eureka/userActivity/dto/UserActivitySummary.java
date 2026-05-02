package com.example.eureka.userActivity.dto;

import java.time.LocalDateTime;

public class UserActivitySummary {
    private Long userId;
    private String username;
    private Long totalActions;
    private Long orderCount;
    private LocalDateTime lastSeen;
    public UserActivitySummary(){}


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(Long totalActions) {
        this.totalActions = totalActions;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}