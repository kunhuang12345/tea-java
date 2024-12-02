package com.bbs.teajava.utils;

import com.bbs.teajava.entity.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author kunhuang
 */
public class SessionUtils {

    public static HttpSession getSession() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest().getSession();
    }

    public static Users getUser() {
        HttpSession session = getSession();
        return (Users) session.getAttribute("user");
    }


    private SessionUtils() {
    }

}
