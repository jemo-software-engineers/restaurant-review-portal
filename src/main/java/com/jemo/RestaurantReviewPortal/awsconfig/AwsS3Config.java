package com.jemo.RestaurantReviewPortal.awsconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class AwsS3Config {
    // for local environment
//    private final Dotenv dotenv;
//
//    public AwsS3Config() {
//        this.dotenv = Dotenv.configure().load();
//    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                // for production environment
                System.getenv("AWS_ACCESS_KEY"),
                System.getenv("AWS_SECRET_KEY")

                // for local environment
//                dotenv.get("AWS_ACCESS_KEY"),
//                dotenv.get("AWS_SECRET_KEY")

        );
        return S3Client.builder()
                .region(
                        Region.of(
                                // for production environment
                                System.getenv("AWS_REGION")

                                // for local environment
//                                 dotenv.get("AWS_REGION")
                        ))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public String bucketName() {
        // for production environment
        return System.getenv("AWS_BUCKET_NAME");

        // for local environment
//        return dotenv.get("AWS_BUCKET_NAME");
    }
}
