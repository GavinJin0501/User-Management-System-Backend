package com.gavinjin.backend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User-Team relation table
 * @TableName user_team
 */
@TableName(value ="user_team")
@Data
public class UserTeam implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User id in the team
     */
    private Long userid;

    /**
     * Team id
     */
    private Long teamid;

    /**
     * Time of join in the team
     */
    private Date joinedTime;

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