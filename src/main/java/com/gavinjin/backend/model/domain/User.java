package com.gavinjin.backend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * Account is deleted logically
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}