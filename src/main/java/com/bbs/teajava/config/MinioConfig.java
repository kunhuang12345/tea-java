package com.bbs.teajava.config;

import io.minio.MinioClient;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.*;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * @author kunhuang
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig implements CommandLineRunner {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private List<BucketConfig> buckets;
    @Resource
    @Lazy
    private MinioClient minioClient;
    @Resource
    private ParamConfig param;

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

    @Override
    public void run(String... args) throws Exception {
        String bucketName = param.getMinioTemp();

        // 创建明天这个时间的过期时间
        String tomorrowDate = ZonedDateTime.now(ZoneOffset.UTC)  // 使用 UTC 时区
                .plusDays(1)
                .truncatedTo(ChronoUnit.DAYS)    // 截断到天（即午夜）
                .format(DateTimeFormatter.ISO_INSTANT);
        // 创建单个规则
        LifecycleRule rule = new LifecycleRule(
                Status.ENABLED,                          // 启用规则
                null,                                   // 不设置分片上传
                new Expiration(
                        ResponseDate.fromString(tomorrowDate),  // 明天这个时间过期
                        null,                                   // 不使用天数
                        null                                   // 不删除过期标记
                ),
                new RuleFilter(null, "", null),      // 空过滤器，应用到所有文件
                "expire-all",                           // 规则ID
                null,                                   // 不设置版本过期
                null,                                   // 不设置版本转换
                null                                    // 不设置转换
        );
        // 应用配置
        LifecycleConfiguration config = new LifecycleConfiguration(Collections.singletonList(rule));
        minioClient.setBucketLifecycle(
                SetBucketLifecycleArgs.builder()
                        .bucket(bucketName)
                        .config(config)
                        .build()
        );
    }
}
