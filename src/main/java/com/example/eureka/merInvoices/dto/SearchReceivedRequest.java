package com.example.eureka.merInvoices.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SearchReceivedRequest {

        private int pageNumber;
        private int pageSize;
        private List<String> documentStatuses;
        private List<String> documentTypes;
        private String dateFrom;
        private String dateTo;
        private String searchText;

        private String senderName;
        private String senderIdentifierValue;
        private int senderIdentifierType;
        private String internalMark;
        private String dueDateFrom;
        private String dueDateTo;
        private Boolean isHighImportanceReceive;
        private String receivedDateFrom;
        private String receivedDateTo;
        private String recipientEmail;
        private String sentDateFrom;
        private String sentDateTo;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getDocumentStatuses() {
        return documentStatuses;
    }

    public void setDocumentStatuses(List<String> documentStatuses) {
        this.documentStatuses = documentStatuses;
    }

    public List<String> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<String> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
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

    public int getSenderIdentifierType() {
        return senderIdentifierType;
    }

    public void setSenderIdentifierType(int senderIdentifierType) {
        this.senderIdentifierType = senderIdentifierType;
    }

    public String getInternalMark() {
        return internalMark;
    }

    public void setInternalMark(String internalMark) {
        this.internalMark = internalMark;
    }

    public String getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(String dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public String getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(String dueDateTo) {
        this.dueDateTo = dueDateTo;
    }

    public Boolean getHighImportanceReceive() {
        return isHighImportanceReceive;
    }

    public void setHighImportanceReceive(Boolean highImportanceReceive) {
        isHighImportanceReceive = highImportanceReceive;
    }

    public String getReceivedDateFrom() {
        return receivedDateFrom;
    }

    public void setReceivedDateFrom(String receivedDateFrom) {
        this.receivedDateFrom = receivedDateFrom;
    }

    public String getReceivedDateTo() {
        return receivedDateTo;
    }

    public void setReceivedDateTo(String receivedDateTo) {
        this.receivedDateTo = receivedDateTo;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSentDateFrom() {
        return sentDateFrom;
    }

    public void setSentDateFrom(String sentDateFrom) {
        this.sentDateFrom = sentDateFrom;
    }

    public String getSentDateTo() {
        return sentDateTo;
    }

    public void setSentDateTo(String sentDateTo) {
        this.sentDateTo = sentDateTo;
    }
}
