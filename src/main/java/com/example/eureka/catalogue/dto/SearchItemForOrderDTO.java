package com.example.eureka.catalogue.dto;

public class SearchItemForOrderDTO {
    private Long id;
    private String code;
    private String name;
    private String source;


    public SearchItemForOrderDTO() {}

    public SearchItemForOrderDTO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
