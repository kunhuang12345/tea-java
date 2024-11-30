package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */
public enum FriendRequestStatusEnum {
    // 待处理
    PENDING(0),
    // 已接受
    ACCEPTED(1),
    // 已拒绝
    REJECTED(2);

    private final int status;

    FriendRequestStatusEnum(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }
}
