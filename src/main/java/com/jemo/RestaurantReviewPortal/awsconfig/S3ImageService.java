package com.jemo.RestaurantReviewPortal.awsconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

@Service
public class S3ImageService {
    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public S3ImageService(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFileName);

        try {
            // Validate file type (optional)
            String mimeType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            if (mimeType == null || !mimeType.startsWith("image")) {
                throw new IllegalArgumentException("Only image files are allowed.");
            }

            // Upload to S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(uniqueFileName)
                            .contentType(mimeType)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            file.getInputStream(), file.getSize()
                    )
            );

            return "https://" + bucketName + ".s3."  + "us-east-1.amazonaws.com/" + uniqueFileName;

        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }


    // Method to sanitize and rename file name
    private String generateUniqueFileName(String originalFileName) {
        // Get the file extension (e.g., ".jpg")
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        // Replace spaces with hyphens and generate a unique file name
        String baseName = originalFileName.substring(0, lastDotIndex).replaceAll("\\s+", "-");
        String uniqueName = baseName + "-" + UUID.randomUUID().toString() + extension;

        return uniqueName;
    }
}
