package com.gavinjin.backend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User register request body
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -4926403245029972172L;

    private String userAccount;

    private String userPassword;
}
