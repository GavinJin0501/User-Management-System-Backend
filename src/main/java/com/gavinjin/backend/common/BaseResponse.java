package com.gavinjin.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Common response class
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -2888840582685378854L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(int code, String message, String description) {
        this(code, null, message, description);
    }

    public BaseResponse(StatusCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
