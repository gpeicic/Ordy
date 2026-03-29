package com.example.eureka.merInvoices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceSummary {
    private Long id;
    private Integer status;
    private String statusLabel;
    private Integer documentType;
    private String documentTypeLabel;
    private Integer businessStatus;
    private String businessStatusLabel;
    private String senderName;
    private String senderIdentifierValue;
    private Integer senderIdentifierType;
    @JsonFormat(pattern = "dd. MM. yyyy. HH:mm:ss")
    private LocalDateTime sentDateTime;
    @JsonFormat(pattern = "dd. MM. yyyy. HH:mm:ss")
    private LocalDateTime receivedDateTime;
    @JsonFormat(pattern = "dd. MM. yyyy. HH:mm:ss")
    private LocalDateTime dueDateTime;
    private List<String> recipientEmails;
    private String internalMark;
    private Boolean isHir;
    private Integer attachmentCount;
    private Boolean shouldTaxCorrectionStatementBeCreated;
    private Boolean allowWmdExport;
    private Boolean canPay;
    private Integer inboundFiscalizationStatus;
    private String inboundFiscalizationStatusLabel;
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeLabel() {
        return documentTypeLabel;
    }

    public void setDocumentTypeLabel(String documentTypeLabel) {
        this.documentTypeLabel = documentTypeLabel;
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getBusinessStatusLabel() {
        return businessStatusLabel;
    }

    public void setBusinessStatusLabel(String businessStatusLabel) {
        this.businessStatusLabel = businessStatusLabel;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderIdentifierValue() {
        return senderIdentifierValue;
    }

    public void setSenderIdentifierValue(String senderIdentifierValue) {
        this.senderIdentifierValue = senderIdentifierValue;
    }

    public Integer getSenderIdentifierType() {
        return senderIdentifierType;
    }

    public void setSenderIdentifierType(Integer senderIdentifierType) {
        this.senderIdentifierType = senderIdentifierType;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public LocalDateTime getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(LocalDateTime receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(List<String> recipientEmails) {
        this.recipientEmails = recipientEmails;
    }

    public String getInternalMark() {
        return internalMark;
    }

    public void setInternalMark(String internalMark) {
        this.internalMark = internalMark;
    }

    public Boolean getHir() {
        return isHir;
    }

    public void setHir(Boolean hir) {
        isHir = hir;
    }

    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public Boolean getShouldTaxCorrectionStatementBeCreated() {
        return shouldTaxCorrectionStatementBeCreated;
    }

    public void setShouldTaxCorrectionStatementBeCreated(Boolean shouldTaxCorrectionStatementBeCreated) {
        this.shouldTaxCorrectionStatementBeCreated = shouldTaxCorrectionStatementBeCreated;
    }

    public Boolean getAllowWmdExport() {
        return allowWmdExport;
    }

    public void setAllowWmdExport(Boolean allowWmdExport) {
        this.allowWmdExport = allowWmdExport;
    }

    public Boolean getCanPay() {
        return canPay;
    }

    public void setCanPay(Boolean canPay) {
        this.canPay = canPay;
    }

    public Integer getInboundFiscalizationStatus() {
        return inboundFiscalizationStatus;
    }

    public void setInboundFiscalizationStatus(Integer inboundFiscalizationStatus) {
        this.inboundFiscalizationStatus = inboundFiscalizationStatus;
    }

    public String getInboundFiscalizationStatusLabel() {
        return inboundFiscalizationStatusLabel;
    }

    public void setInboundFiscalizationStatusLabel(String inboundFiscalizationStatusLabel) {
        this.inboundFiscalizationStatusLabel = inboundFiscalizationStatusLabel;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
