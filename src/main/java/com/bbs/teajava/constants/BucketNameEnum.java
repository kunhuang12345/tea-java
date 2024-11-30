package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */

public enum BucketNameEnum {
    TEMP("temp"),
    PAPER("paper"),
    ATTACHMENT("attachment");

    private final String bucketName;

    BucketNameEnum(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getValue() {
        return bucketName;
    }

}
