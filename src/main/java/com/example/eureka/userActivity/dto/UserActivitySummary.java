package com.example.eureka.userActivity.dto;

import java.time.LocalDateTime;

public class UserActivitySummary {
    private Long userId;
    private String firstName;
    private String lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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