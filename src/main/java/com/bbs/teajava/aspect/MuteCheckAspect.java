package com.bbs.teajava.aspect;

import com.bbs.teajava.annotation.MuteCheck;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.exception.AccessDeniedException;
import com.bbs.teajava.utils.RedisUtil;
import com.bbs.teajava.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author kunhuang
 */
@Aspect
@Component
@RequiredArgsConstructor
@Order(2)
public class MuteCheckAspect {

    private final RedisUtil redis;

    /**
     * 检查是否被禁言
     * @param joinPoint 方法调用点
     * @param muteCheck 禁言检测
     * @return 方法调用
     */
    @Around("@annotation(muteCheck)")
    public Object checkMute(ProceedingJoinPoint joinPoint, MuteCheck muteCheck) throws Throwable {
        Users user = SessionUtils.getUser();
        String value = redis.get("mute" + user.getId());
        if (StringUtils.isNotEmpty(value)) {
            throw new AccessDeniedException("您已被禁言");
        }
        return joinPoint.proceed();
    }

}
