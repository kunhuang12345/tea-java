package com.bbs.teajava.annotation;

import java.lang.annotation.*;

/**
 * @author kunhuang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authentication {
    boolean requireAdmin() default false;
}
