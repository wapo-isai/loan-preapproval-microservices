//package com.loanapproval.userservice.service;
//
//import com.loanapproval.userservice.dto.UserDocumentResponse;
//import com.loanapproval.userservice.persistence.DocumentMetadata;
//import com.loanapproval.userservice.persistence.DocumentMetadataRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.util.StringUtils; // For cleaning filename
//
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class DocumentService {
//    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
//
////    @Autowired
////    private S3Service s3Service;
//
//    @Autowired
//    private DocumentMetadataRepository metadataRepository;
//
//    // Consider defining allowed types and max size in application properties
//    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("application/pdf", "image/jpeg", "image/png");
//    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB example
//
//    public DocumentMetadata uploadDocument(String userId, String documentType, MultipartFile file) throws IOException {
//        if (userId == null || userId.trim().isEmpty()) {
//            throw new IllegalArgumentException("User ID cannot be null or empty.");
//        }
//        if (documentType == null || documentType.trim().isEmpty()) {
//            throw new IllegalArgumentException("Document type cannot be null or empty.");
//        }
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("Cannot upload an empty file.");
//        }
//
//        // Validate file type
//        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
//            throw new IllegalArgumentException("Invalid file type: " + file.getContentType() + ". Allowed types are: " + ALLOWED_CONTENT_TYPES);
//        }
//
//        // Validate file size
//        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
//            throw new IllegalArgumentException("File size exceeds the limit of " + (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + "MB.");
//        }
//
//        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
//        String fileExtension = "";
//        int i = originalFileName.lastIndexOf('.');
//        if (i > 0) {
//            fileExtension = originalFileName.substring(i);
//        }
//
//        // Construct a unique S3 key
//        String s3Key = String.format("user-docs/%s/%s/%s%s",
//                userId,
//                documentType.toLowerCase().replace(" ", "-"),
//                UUID.randomUUID().toString(), // Ensures uniqueness
//                fileExtension);
//
//        // Upload to S3
//        s3Service.uploadFile(file, s3Key); // This will throw IOException on failure
//
//        // Save metadata to MongoDB
//        DocumentMetadata metadata = new DocumentMetadata(
//                userId,
//                documentType,
//                s3Key,
//                originalFileName,
//                file.getContentType(),
//                file.getSize()
//        );
//        DocumentMetadata savedMetadata = metadataRepository.save(metadata);
//        logger.info("Document metadata saved for user {}, type {}: {}", userId, documentType, savedMetadata.getId());
//        return savedMetadata;
//    }
//
//    public List<UserDocumentResponse> getUserDocumentsWithPresignedUrls(String userId) {
//        if (userId == null || userId.trim().isEmpty()) {
//            throw new IllegalArgumentException("User ID cannot be null or empty.");
//        }
//        List<DocumentMetadata> metadataList = metadataRepository.findByUserId(userId);
//        return metadataList.stream().map(metadata -> {
//            String presignedUrl = s3Service.generatePresignedUrl(metadata.getS3Key());
//            return new UserDocumentResponse(
//                    metadata.getId(),
//                    metadata.getUserId(),
//                    metadata.getDocumentType(),
//                    metadata.getFileName(),
//                    metadata.getContentType(),
//                    metadata.getSize(),
//                    metadata.getUploadedAt(),
//                    presignedUrl // This can be null if presigned URL generation fails
//            );
//        }).collect(Collectors.toList());
//    }
//}
