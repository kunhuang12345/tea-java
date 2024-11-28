package com.bbs.teajava.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

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
     * @param bucketName 桶名
     * @param fileName 文件命
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
