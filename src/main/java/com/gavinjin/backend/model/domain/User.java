package com.gavinjin.backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User table
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * User password
     */
    private String userPassword;

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
     * Account is deleted logically
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}