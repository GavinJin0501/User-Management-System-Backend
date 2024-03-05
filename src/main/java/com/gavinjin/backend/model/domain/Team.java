package com.gavinjin.backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Team table
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Team name
     */
    private String name;

    /**
     * Team description
     */
    private String description;

    /**
     * Max team size
     */
    private Integer maxNum;

    /**
     * Expiration time
     */
    private Date expiration;

    /**
     * Team creator id
     */
    private Long userid;

    /**
     * 0: public, 1: private, 2: password
     */
    private Integer status;

    /**
     * Password to enter the team (if any)
     */
    private String password;

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
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}