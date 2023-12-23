package com.gavinjin.backend.exception;

import com.gavinjin.backend.common.StatusCode;
import lombok.Getter;

/**
 * Customized exception class
 *
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(StatusCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription());
    }

    public BusinessException(StatusCode errorCode, String description) {
        this(errorCode.getCode(), errorCode.getMessage(), description);
    }

}
