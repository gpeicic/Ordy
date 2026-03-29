package com.example.eureka.user;

public class User {
    private Long id;
    private String username;
    private String password;
    private Long role_id;
    private Long company_id;

    public User(){}

    public User(Long id, String username, String password, Long role_id, Long company_id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role_id = role_id;
        this.company_id = company_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }
}
