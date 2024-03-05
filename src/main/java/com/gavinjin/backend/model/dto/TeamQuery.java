package com.gavinjin.backend.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.gavinjin.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Team query dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * id
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
     * Max team size
     */
    private Integer maxNum;


    /**
     * Team creator id
     */
    private Long userid;

    /**
     * 0: public, 1: private, 2: password
     */
    private Integer status;
}