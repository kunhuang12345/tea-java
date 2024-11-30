package com.bbs.teajava.utils;

/**
 * @author kunhuang
 */
public class FilePathUtils {

    /**
     * 文件路径规范：attachments/{email}/{fileName}
     *
     * @param email 邮箱
     * @param fileName 文件名
     * @return 文件路径
     */
    public static String attachmentPath(String email, String fileName) {
        return "attachments/" + email + "/" + fileName;
    }

    public static String paperPath(String email, String fileName) {
        return "papers/" + email + "/" + fileName;
    }

    private FilePathUtils() {
    }
}
