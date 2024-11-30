package com.bbs.teajava.utils;

public class ApiResultUtils {
    private int code;
    private Object data;
    private String message;
    private int rows;

    public ApiResultUtils(int code) {
        this.code = code;

    }

    public ApiResultUtils(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResultUtils(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static ApiResultUtils success() {
        return new ApiResultUtils(200, "success");
    }

    public static ApiResultUtils success(Object data) {
        return new ApiResultUtils(200, data, "success");
    }

    public static ApiResultUtils success(int rows) {
        ApiResultUtils apiResultUtils = new ApiResultUtils(200, "success");
        apiResultUtils.setRows(rows);
        return apiResultUtils;
    }

    public static ApiResultUtils error(int code, String message) {
        return new ApiResultUtils(code, message);
    }

    public static ApiResultUtils error() {
        return new ApiResultUtils(500, "error");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
