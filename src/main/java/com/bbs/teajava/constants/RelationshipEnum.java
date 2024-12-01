package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */
public enum RelationshipEnum {
    // 非朋友关系
    NOT_FRIENDS(0),
    // 朋友关系
    FRIENDS(1),
    // 未关注
    NOT_FOLLOW(2),
    // 关注
    FOLLOW(3);

    private final int value;

    private RelationshipEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
