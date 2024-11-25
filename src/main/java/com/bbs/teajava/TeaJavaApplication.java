package com.bbs.teajava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bbs.teajava.dao")
public class TeaJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaJavaApplication.class, args);
    }

}
