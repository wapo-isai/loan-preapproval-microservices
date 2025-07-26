//package com.loanapproval.userservice.controller;
//
//import com.loanapproval.userservice.dto.UserDocumentResponse;
//import com.loanapproval.userservice.persistence.DocumentMetadata;
//import com.loanapproval.userservice.service.DocumentService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users/documents") // This path will be routed by the Gateway
//public class DocumentController {
//    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
//
//    @Autowired
//    private DocumentService documentService;
//
//    // Endpoint to upload a document
//    // The userId could also be extracted from the JWT principal if Spring Security is configured for that here
//    // or passed as a header by the gateway (e.g., X-Authenticated-User-Id)
//    @PostMapping
//    public ResponseEntity<?> uploadDocument(
//            @RequestHeader("X-Authenticated-User-Id") String userId, // Assuming Gateway adds this header
//            @RequestParam("documentType") String documentType,
//            @RequestParam("file") MultipartFile file) {
//        logger.info("Received request to upload document for user: {}, type: {}", userId, documentType);
//        if (userId == null || userId.trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User ID missing or user not authenticated."));
//        }
//        try {
//            DocumentMetadata metadata = documentService.uploadDocument(userId, documentType, file);
//            // Return the metadata, or a simplified response
//            return ResponseEntity.status(HttpStatus.CREATED).body(metadata);
//        } catch (IllegalArgumentException e) {
//            logger.warn("Upload failed (validation): {}", e.getMessage());
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        } catch (IOException e) {
//            logger.error("Upload failed (I/O or S3 issue): {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to upload document due to a server error."));
//        } catch (Exception e) {
//            logger.error("Unexpected error during document upload: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
//        }
//    }
//
//    // Endpoint to get all documents for the authenticated user
//    @GetMapping
//    public ResponseEntity<?> getUserDocuments(@RequestHeader("X-Authenticated-User-Id") String userId) {
//        logger.info("Received request to get documents for user: {}", userId);
//        if (userId == null || userId.trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User ID missing or user not authenticated."));
//        }
//        try {
//            List<UserDocumentResponse> documents = documentService.getUserDocumentsWithPresignedUrls(userId);
//            return ResponseEntity.ok(documents);
//        } catch (IllegalArgumentException e) {
//            logger.warn("Get documents failed (validation): {}", e.getMessage());
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            logger.error("Unexpected error fetching documents: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
//        }
//    }
//}
