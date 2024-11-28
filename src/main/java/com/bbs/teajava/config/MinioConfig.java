package com.bbs.teajava.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author kunhuang
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private List<BucketConfig> buckets;

    @Data
    public static class BucketConfig {
        private String name;
        private String policy;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
