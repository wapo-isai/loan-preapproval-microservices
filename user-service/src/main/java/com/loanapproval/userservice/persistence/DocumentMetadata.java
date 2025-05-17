package com.loanapproval.userservice.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "documentMetadata")
public class DocumentMetadata {
    @Id
    private String id; // MongoDB ObjectId will be generated

    private String userId;
    private String documentType; // e.g., "proof-of-income", "credit-report"
    private String s3Key;        // Key used in S3 (path + filename)
    private String fileName;     // Original uploaded file name
    private String contentType;  // MIME type of the file
    private long size;           // File size in bytes
    private Date uploadedAt;

    public DocumentMetadata(String userId, String documentType, String s3Key, String fileName, String contentType, long size) {
        this.userId = userId;
        this.documentType = documentType;
        this.s3Key = s3Key;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = new Date();
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

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
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
}
