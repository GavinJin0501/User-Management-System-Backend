package com.gavinjin.backend.constant;

/**
 * User constants
 */
public interface UserConstant {
    /**
     * Session attribute key for user login state
     */
    String USER_LOGIN_STATE = "userLoginState";


    // ------- Access control roles -------
    /**
     * Default role
     */
    int DEFAULT_ROLE = 0;

    /**
     * Administrator role
     */
    int ADMIN_ROLE = 1;

    /**
     * VIP role
     */
    int VIP_ROLE = 2;
}
