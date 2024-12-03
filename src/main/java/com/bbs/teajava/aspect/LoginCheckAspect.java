package com.bbs.teajava.aspect;

import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.constants.RoleEnum;
import com.bbs.teajava.entity.Users;
import com.bbs.teajava.exception.AccessDeniedException;
import com.bbs.teajava.exception.UnauthorizedException;
import com.bbs.teajava.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
@Order(1)
public class LoginCheckAspect {

    @Around("@annotation(authentication)")
    public Object checkLogin(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable {
        HttpSession session = SessionUtils.getSession();
        Users userInSession = (Users) session.getAttribute("user");
        if (userInSession == null) {
            throw new UnauthorizedException("请先登录");
        }
        if (authentication.requireAdmin() && this.isAdmin(userInSession)) {
            throw new AccessDeniedException("权限不足");
        }
        if (authentication.requireReporter() && this.isReporter(userInSession) && this.isAdmin(userInSession)) {
            throw new AccessDeniedException("请先注册会议报告者");
        }
        return joinPoint.proceed();
    }

    private boolean isAdmin(Users user) {
        return user.getRole() != RoleEnum.ADMIN.getValue();
    }

    private boolean isReporter(Users user) {
        return user.getRole() != RoleEnum.REPORTER.getValue();
    }
}
