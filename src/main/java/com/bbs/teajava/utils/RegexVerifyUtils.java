package com.bbs.teajava.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunhuang
 */
public class RegexVerifyUtils {
    // 常用正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z]\\w{5,19}$";
    private static final String ID_CARD_REGEX = "^(\\d{15}|\\d{18}|\\d{17}([\\dXx]))$";
    private static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    private RegexVerifyUtils(){}


    /**
     * 密码合格校验
     *
     * @param password 密码
     * @return 错误信息列表
     */
    public static List<String> validatePassword(String password) {
        List<String> validationErrors = new ArrayList<>();

        if (password.length() < 8) {
            validationErrors.add("密码长度不能小于8位");
        }
        if (password.length() > 20) {
            validationErrors.add("密码长度不能小于20位");
        }
        if (!password.matches(".*[a-z].*")) {
            validationErrors.add("必须包含小写字母");
        }
        if (!password.matches(".*\\d.*")) {
            validationErrors.add("必须包含数字");
        }
        return validationErrors;
    }

    /**
     * 校验邮箱格式
     * @param email 待校验的邮箱
     * @return 校验结果
     */
    public static boolean isEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    /**
     * 校验密码强度
     * 要求：至少8位，包含大小写字母和数字
     * @param password 待校验的密码
     * @return 校验结果
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }

    /**
     * 校验手机号（中国大陆）
     * @param phone 待校验的手机号
     * @return 校验结果
     */
    public static boolean isPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches(PHONE_REGEX);
    }

    /**
     * 校验用户名
     * 要求：字母开头，允许字母数字下划线，6-20位
     * @param username 待校验的用户名
     * @return 校验结果
     */
    public static boolean isUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches(USERNAME_REGEX);
    }

    /**
     * 校验身份证号
     * @param idCard 待校验的身份证号
     * @return 校验结果
     */
    public static boolean isIdCard(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }
        return idCard.matches(ID_CARD_REGEX);
    }

    /**
     * 校验日期格式 (YYYY-MM-DD)
     * @param date 待校验的日期字符串
     * @return 校验结果
     */
    public static boolean isDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        return date.matches(DATE_REGEX);
    }

    /**
     * 自定义正则校验
     * @param value 待校验的值
     * @param regex 正则表达式
     * @return 校验结果
     */
    public static boolean validate(String value, String regex) {
        if (value == null || regex == null) {
            return false;
        }
        return value.matches(regex);
    }
}
