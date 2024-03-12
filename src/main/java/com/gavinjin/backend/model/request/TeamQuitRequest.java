package com.gavinjin.backend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Team quit request
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -3842326840430227097L;

    /**
     * id
     */
    private Long teamId;

}