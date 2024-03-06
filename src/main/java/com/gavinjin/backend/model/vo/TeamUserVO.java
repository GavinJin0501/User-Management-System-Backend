package com.gavinjin.backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Team use information
 */
@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = 8779281824177088542L;
    /**
     *
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
     * Account created time
     */
    private Date createdTime;

    /**
     * Account updated time
     */
    private Date updatedTime;

    /**
     * User list of the team
     */
    UserVO teamCreator;
}
