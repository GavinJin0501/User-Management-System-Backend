package com.gavinjin.backend.common;

import lombok.Getter;

/**
 * Global status code for all responses
 *
 */
@Getter
public enum StatusCode {
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "Request parameters error", ""),
    NULL_ERROR(40001, "Request is null", ""),
    NOT_LOGGED_IN(40100, "Not logged in", ""),
    NO_AUTH(40101, "No authorization", ""),
    SYSTEM_ERROR(50000, "Internal server error", "");

    /**
     * Status code of the response
     */
    private final int code;

    /**
     * Message of the status code
     */
    private final String message;

    /**
     * Detailed description of the status code
     */
    private final String description;

    StatusCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
