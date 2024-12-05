package com.bbs.teajava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kunhuang
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@EnableScheduling  // 开启定时任务
@MapperScan("com.bbs.teajava.mapper")
public class TeaJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaJavaApplication.class, args);
    }

}
