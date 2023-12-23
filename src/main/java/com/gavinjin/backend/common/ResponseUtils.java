package com.gavinjin.backend.common;

/**
 * Response utils
 *
 */
public class ResponseUtils {
    /**
     * Basic success response
     *
     * @param data Data to return to the client
     * @return A base response
     * @param <T> Type of the data to return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    /**
     * Basic error response
     *
     * @param errorCode
     * @return
     */
    public static <T> BaseResponse<T> error(StatusCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }

    public static <T> BaseResponse<T> error(StatusCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }

    public static <T> BaseResponse<T> error(StatusCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), message, description);
    }
}
