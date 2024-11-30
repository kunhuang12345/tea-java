package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */

public enum RoleEnum {
    // 普通用户
    USER(1),
    // 管理员
    ADMIN(2),
    // 报告者
    REPORTER(3);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
