package com.bbs.teajava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kunhuang
 */
@Component
public class ParamConfig {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.properties.mail.sendName:茶偶通知}")
    private String sendName;

    public String getFromEmail() {
        return fromEmail;
    }

    public String getSendName() {
        return sendName;
    }
}

