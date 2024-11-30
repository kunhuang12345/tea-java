package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */

public enum ReporterApplyStatusEnum {
    // 待审核：0
    WAIT_AUDIT(0),
    // 审核通过：1
    AUDIT_PASS(1),
    // 审核不通过：2
    AUDIT_NOT_PASS(2);

    private final int value;

    ReporterApplyStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
