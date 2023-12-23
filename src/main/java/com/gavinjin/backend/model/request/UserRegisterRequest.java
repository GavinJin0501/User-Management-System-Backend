package com.gavinjin.backend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User register request body
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 9088799198827061387L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
