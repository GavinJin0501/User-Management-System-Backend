package com.gavinjin.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * General page request parameters
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -7132400981895604735L;

    /**
     * Size of each page
     */
    protected int pageSize = 10;

    /**
     * Current page index
     */
    protected int pageNum = 1;
}
