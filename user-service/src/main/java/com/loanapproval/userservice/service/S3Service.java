//package com.loanapproval.userservice.service;
//
//import jakarta.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
//
//import java.io.IOException;
//import java.time.Duration;
//
//@Service
//public class S3Service {
//    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
//
//    private S3Client s3Client;
//    private S3Presigner s3Presigner;
//
//    @Value("${aws.region}")
//    private String awsRegion;
//
//    @Value("${aws.s3.bucket-name}")
//    private String bucketName;
//
//    @Value("${aws.s3.presigned-url-duration-minutes:15}") // Default to 15 minutes
//    private int presignedUrlDurationMinutes;
//
//    @PostConstruct
//    private void initializeClients() {
//        try {
//            Region region = Region.of(awsRegion);
//            this.s3Client = S3Client.builder()
//                    .region(region)
//                    .credentialsProvider(DefaultCredentialsProvider.create()) // Uses default credential chain
//                    .build();
//            this.s3Presigner = S3Presigner.builder()
//                    .region(region)
//                    .credentialsProvider(DefaultCredentialsProvider.create())
//                    .build();
//            logger.info("S3Client and S3Presigner initialized successfully for region: {}", awsRegion);
//        } catch (Exception e) {
//            logger.error("Error initializing AWS S3 clients: {}", e.getMessage(), e);
//            // Handle initialization failure appropriately, maybe prevent application startup
//        }
//    }
//
//    public String uploadFile(MultipartFile file, String s3Key) throws IOException {
//        if (s3Client == null) {
//            throw new IllegalStateException("S3Client not initialized. Check AWS configuration.");
//        }
//        try {
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(s3Key)
//                    .contentType(file.getContentType())
//                    // .acl(ObjectCannedACL.PRIVATE) // Default is private, explicitly setting can be good
//                    .build();
//
//            PutObjectResponse response = s3Client.putObject(putObjectRequest,
//                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//
//            logger.info("File uploaded successfully to S3. Key: {}, ETag: {}", s3Key, response.eTag());
//            // Construct and return the full S3 URL (or just the key, depending on needs)
//            // For this flow, just the key is fine as we generate presigned URLs for access
//            return s3Key;
//        } catch (Exception e) {
//            logger.error("Error uploading file to S3. Key: {}: {}", s3Key, e.getMessage(), e);
//            throw new IOException("Failed to upload file to S3: " + e.getMessage(), e);
//        }
//    }
//
//    public String generatePresignedUrl(String s3Key) {
//        if (s3Presigner == null) {
//            throw new IllegalStateException("S3Presigner not initialized. Check AWS configuration.");
//        }
//        try {
//            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(s3Key)
//                    .build();
//
//            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
//                    .signatureDuration(Duration.ofMinutes(presignedUrlDurationMinutes))
//                    .getObjectRequest(getObjectRequest)
//                    .build();
//
//            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
//            String url = presignedRequest.url().toString();
//            logger.debug("Generated presigned URL for key {}: {}", s3Key, url);
//            return url;
//        } catch (Exception e) {
//            logger.error("Error generating presigned URL for key {}: {}", s3Key, e.getMessage(), e);
//            // Depending on requirements, you might return null or throw a custom exception
//            return null;
//        }
//    }
//}
