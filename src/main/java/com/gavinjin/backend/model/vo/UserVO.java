package com.gavinjin.backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User table
 * @TableName user
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * User nickname
     */
    private String username;

    /**
     * User account
     */
    private String userAccount;

    /**
     * Url of avatar
     */
    private String avatarUrl;

    /**
     * Gender
     */
    private Integer gender;

    /**
     * Phone number
     */
    private String phone;

    /**
     * User email
     */
    private String email;

    /**
     * Account status: default is 0 - normal
     */
    private Integer userStatus;

    /**
     * Account created time
     */
    private Date createdTime;

    /**
     * Account updated time
     */
    private Date updatedTime;

    /**
     * Role of the user:
     *  0: Normal User
     *  1: Administrator
     *  2: VIP
     */
    private Integer userRole;

    /**
     * Code of the planet
     */
    private String planetCode;

    /**
     * Tags of the user (json)
     */
    private String tags;

    private static final long serialVersionUID = 1L;
}