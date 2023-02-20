package com.lele.gobang.common;


/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> success(T data) {
        return new R<>(0, data, "ok");
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> success() {
        return new R<>(0, "", "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static R error(ErrorCode errorCode) {
        return new R<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static R error(int code, String message, String description) {
        return new R(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static R error(ErrorCode errorCode, String message, String description) {
        return new R(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static R error(ErrorCode errorCode, String description) {
        return new R(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
