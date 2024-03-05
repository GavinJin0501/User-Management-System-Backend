package com.gavinjin.backend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamAddRequest implements Serializable {
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

    private static final long serialVersionUID = 1L;
}
