package com.gavinjin.backend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Team join request
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -3842326840430227097L;

    /**
     * id
     */
    private Long teamId;

    /**
     * Password to enter the team (if any)
     */
    private String password;


}