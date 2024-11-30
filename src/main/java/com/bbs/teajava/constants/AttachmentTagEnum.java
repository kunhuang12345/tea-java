package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */
public enum AttachmentTagEnum {
    // 存在附件
    EXIST(1),
    NOT_EXIST(0);
    private final int value;

    private AttachmentTagEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }



}
