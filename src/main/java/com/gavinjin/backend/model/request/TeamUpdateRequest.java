package com.gavinjin.backend.model.request;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Team table
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 4677370282805951651L;

    /**
     * Team id
     */
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
     * Expiration time
     */
    private Date expiration;

    /**
     * 0: public, 1: private, 2: password
     */
    private Integer status;

    /**
     * Password to enter the team (if any)
     */
    private String password;
}