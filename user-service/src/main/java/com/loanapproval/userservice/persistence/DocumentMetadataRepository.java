package com.loanapproval.userservice.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentMetadataRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findByUserId(String userId);
    List<DocumentMetadata> findByUserIdAndDocumentType(String userId, String documentType);
}
