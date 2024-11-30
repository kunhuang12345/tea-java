package com.bbs.teajava.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kunhuang
 */
@Component
@Data
public class ParamConfig {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.properties.mail.sendName:茶偶通知}")
    private String sendName;

    @Value("${minio.buckets[2].name}")
    private String minioTemp;

    public String getFromEmail() {
        return fromEmail;
    }

    public String getSendName() {
        return sendName;
    }
}

