package com.example.eureka.supplier;

public class Supplier {
    private Long id;
    private String oib;
    private String name;
    private String email;

    public Supplier(){}
    public Supplier(Long id, String oib, String name, String email) {
        this.id = id;
        this.oib = oib;
        this.name = name;
        this.email = email;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
