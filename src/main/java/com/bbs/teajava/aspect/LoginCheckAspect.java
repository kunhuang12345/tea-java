package com.bbs.teajava.aspect;

import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.exception.UnauthorizedException;
import com.bbs.teajava.utils.RedisUtil;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class LoginCheckAspect {

    @Autowired
    private RedisUtil redisUtil;

    @Around("@annotation(authentication)")
    public Object checkLogin(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable {
        HttpSession session = SessionUtils.getSession();
        String id = session.getId();
        HttpSession sessionInRedis = redisUtil.getObject(id, HttpSession.class);
        if (sessionInRedis == null) {
            throw new UnauthorizedException("Please login first");
        }
        if (authentication.requireAdmin() && !this.isAdmin(sessionInRedis)) {
                throw new AccessDeniedException("权限不足");
            }
        return joinPoint.proceed();
    }

    private boolean isAdmin(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        return user.getRole() == RoleEnum.ADMIN.getValue();
    }
}
