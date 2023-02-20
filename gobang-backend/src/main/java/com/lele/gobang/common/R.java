package com.lele.gobang.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    public R(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public R(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public R(int code, T data, String message) {
        this(code, data, message, "");
    }

    public R(int code, T data) {
        this(code, data, "", "");
    }


    public R(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
