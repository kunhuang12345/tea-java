package com.bbs.teajava.aspect;

import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.exception.UnauthorizedException;
import com.bbs.teajava.utils.RedisUtil;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

/**
 * @author kunhuang
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAspect {

    private final RedisUtil redisUtil;

    @Around("@annotation(authentication)")
    public Object checkLogin(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable {
        HttpSession session = SessionUtils.getSession();
        String sessionId = session.getId();
        Users userInSession = (Users) session.getAttribute(sessionId);
        if (userInSession == null) {
            throw new UnauthorizedException("请先登录");
        }
        if (authentication.requireAdmin() && !this.isAdmin(userInSession)) {
                throw new AccessDeniedException("权限不足");
            }
        return joinPoint.proceed();
    }

    private boolean isAdmin(Users user) {
        return user.getRole() == RoleEnum.ADMIN.getValue();
    }
}
