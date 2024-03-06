package com.gavinjin.backend.exception;

import com.gavinjin.backend.common.BaseResponse;
import com.gavinjin.backend.common.ResponseUtils;
import com.gavinjin.backend.common.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResponseUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResponseUtils.error(StatusCode.SYSTEM_ERROR, "Internal Server Error", "");
    }
}
