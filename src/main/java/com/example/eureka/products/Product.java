package com.example.eureka.products;

public class Product {
    private Long id;
    private String canonicalName;

    public Product() {}

    public Product(Long id, String canonicalName) {
        this.id = id;
        this.canonicalName = canonicalName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }
}
