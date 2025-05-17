package com.loanapproval.userservice.dto;

import java.util.Date;

public class UserDocumentResponse {
    private String id;
    private String userId;
    private String documentType;
    private String fileName;
    private String contentType;
    private long size;
    private Date uploadedAt;
    private String presignedUrl; // The URL for Angular to use for download

    public UserDocumentResponse(){}

    public UserDocumentResponse(String id, String userId, String documentType, String fileName, String contentType, long size, Date uploadedAt, String presignedUrl) {
        this.id = id;
        this.userId = userId;
        this.documentType = documentType;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
        this.presignedUrl = presignedUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }
}
