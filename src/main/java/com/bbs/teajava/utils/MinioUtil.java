package com.bbs.teajava.utils;

import io.minio.*;
import io.minio.messages.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

/**
 * @author kunhuang
 */
@Component
@RequiredArgsConstructor
public class MinioUtil {
    private final MinioClient minioClient;

    /**
     * 上传文件前检查并创建桶
     *
     * @param bucketName  桶名
     * @param fileName    文件命
     * @param inputStream 文件流
     */
    public void uploadFile(String bucketName, String fileName, InputStream inputStream) {
        try {
            // 确保桶存在
            ensureBucketExists(bucketName);

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }

    /**
     * 在 MinIO 服务器端复制对象
     *
     * @param sourceBucketName 来源桶
     * @param targetBucketName 目标桶
     * @param sourceFilePath   来源文件路径
     * @param targetFilePath   目标文件路径
     */
    public void copyFile(String sourceBucketName, String targetBucketName, String sourceFilePath, String targetFilePath) {
        try {
            // 确保桶存在
            ensureBucketExists(sourceBucketName);
            ensureBucketExists(targetBucketName);

            // 直接在 MinIO 服务器端复制对象
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(targetBucketName)
                            .object(targetFilePath)
                            .source(
                                    CopySource.builder()
                                            .bucket(sourceBucketName)
                                            .object(sourceFilePath)
                                            .build()
                            )
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("复制失败", e);
        }
    }

    /**
     * 根据文件名获取文件
     *
     * @param bucketName 桶名
     * @param fileName   文件名
     * @return 文件流
     */
    public InputStream getObject(String bucketName, String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName).build()
            );
        } catch (Exception e) {
            throw new RuntimeException("下载失败", e);
        }
    }

    public void downLoadFile(String bucketName, String filePath) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            byte[] fileBytes = this.getObject(bucketName, filePath).readAllBytes();
            // 设置文件名（需要URL编码，防止中文乱码）
            String fileName = URLEncoder.encode(filePath, StandardCharsets.UTF_8);

            // 设置响应头
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileName + "\"");

            // 写入数据
            response.getOutputStream().write(fileBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("下载失败", e);
        }
    }

    /**
     * 删除论文文件（设置每月月初1号凌晨过期）
     *
     * @param bucketName 桶名
     * @param fileName   文件名
     */
    public void deleteFile(String bucketName, String fileName) throws Exception {
        // 计算下个月1号凌晨的时间
        ZonedDateTime nextFirstDay = ZonedDateTime.now(ZoneOffset.UTC)
                .plusMonths(1)                             // 下个月
                .withDayOfMonth(1)                         // 月初第一天
                .truncatedTo(ChronoUnit.DAYS);            // 截断到天（凌晨00:00）

        String expirationDate = nextFirstDay.format(DateTimeFormatter.ISO_INSTANT);

        // 创建规则过滤器，指定特定文件
        RuleFilter filter = new RuleFilter(
                null,           // prefix
                fileName,       // 指定文件名
                null           // tags
        );

        // 创建生命周期规则
        LifecycleRule rule = new LifecycleRule(
                Status.ENABLED,                                    // 启用规则
                null,                                             // 不设置分片上传
                new Expiration(
                        ResponseDate.fromString(expirationDate),  // 下月1号凌晨过期
                        null,                                     // 不使用天数
                        null                                      // 不删除过期标记
                ),
                filter,                                          // 使用文件过滤器
                "expire-" + fileName,                            // 规则ID（使用文件名作为一部分）
                null,                                            // 不设置版本过期
                null,                                            // 不设置版本转换
                null                                             // 不设置转换
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


    /**
     * 创建并检查桶
     *
     * @param bucketName 桶名
     */
    private void ensureBucketExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("创建桶失败: " + bucketName, e);
        }
    }
}
