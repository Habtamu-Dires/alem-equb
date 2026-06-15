package com.ekub.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import java.net.URI;

@Configuration
public class S3Config {

    @Value("${s3.space-access-key}")
    String spaceAccessKey;

    @Value("${s3.space-secret-key}")
    String spaceSecretKey;

    @Value("${s3.space-endpoint}")
    String spaceEndPoint;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(
                       spaceAccessKey, spaceSecretKey)
                )
                .endpointOverride(URI.create(spaceEndPoint))
                .region(Region.US_EAST_1)
                .build();
    }
}
