package com.ekub.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket-name}")
    String bucketName;
    @Value("${s3.region}")
    String region;

    // upload file
    public String uploadFile(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unnamed";
        System.out.println("original name: " + originalName);
        String uniqueKey;
        if (originalName.contains(".")) {
            String extension = originalName.substring(originalName.lastIndexOf('.'));
            uniqueKey = UUID.randomUUID() + extension;
        } else {
            uniqueKey = UUID.randomUUID().toString();
        }

        // Prepare the PutObjectRequest with public-read access
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueKey)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        // Upload the file
        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("https://%s.%s.digitaloceanspaces.com/%s", bucketName, region, uniqueKey);
    }

    public void deleteFile(String url){
        String expectedHost = String.format("%s.%s.digitaloceanspaces.com", bucketName, region);

        // Parse the URL
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }

        // Validate the URL belongs to the correct bucket
        if (!uri.getHost().equals(expectedHost)) {
            throw new IllegalArgumentException("URL does not belong to the configured bucket: " + url);
        }

        // Extract the key from the URL
        String key = uri.getPath().substring(1); // Remove leading '/'

        // Prepare and execute the DeleteObjectRequest
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

}
