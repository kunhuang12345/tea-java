package com.bbs.teajava.utils;


/**
 * @author kunhuang
 */
public class FileNameUtils {

    /**
     * 文件命名规范：论文主键+Attachment+文件名
     *
     * @param fileName 初始文件名
     * @param paperKey 论文主键
     * @return 规范化后的文件名
     */
    public static String attachment(String fileName, String paperKey) {
        return paperKey + "Attachment" + fileName;
    }

    public static String paper(String fileName, String paperKey) {
        return paperKey + "Paper" + fileName;
    }

    private FileNameUtils(){}
}
