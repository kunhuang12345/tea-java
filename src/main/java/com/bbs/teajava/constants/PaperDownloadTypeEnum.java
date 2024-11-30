package com.bbs.teajava.constants;

/**
 * @author kunhuang
 */
public enum PaperDownloadTypeEnum {
    // 论文
    PAPER(0),
    // 论文附件
    ATTACHMENT(1);

    private final int value;

    PaperDownloadTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PaperDownloadTypeEnum getEnum(int value) throws Exception {
        for (PaperDownloadTypeEnum type : PaperDownloadTypeEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new Exception("Invalid type value: " + value);
    }
}
