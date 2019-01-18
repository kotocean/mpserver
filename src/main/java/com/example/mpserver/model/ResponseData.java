package com.example.mpserver.model;

/**
 * Created by zhanghd16 on 2018/5/22.
 */
public class ResponseData {
    private int code;
    private String message;

    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Code{
        public final static int SUCCESS = 0;
        public final static int FAILDED = 1;
    }
}
