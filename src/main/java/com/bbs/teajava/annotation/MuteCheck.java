package com.bbs.teajava.annotation;

import java.lang.annotation.*;

/**
 * @author kunhuang
 * 禁言检测注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MuteCheck {
}
