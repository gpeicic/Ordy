package com.example.eureka.merInvoices.dto;

public class DownloadRequest {
    private Long id;
    private Integer documentFileFormat;

    public DownloadRequest(Long id, Integer documentFileFormat) {
        this.id = id;
        this.documentFileFormat = documentFileFormat;
    }

    public Long getId() {
        return id;
    }

    public Integer getDocumentFileFormat() {
        return documentFileFormat;
    }
}
