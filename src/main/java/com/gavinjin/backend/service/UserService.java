package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * User service
 *
* @author gavin
* @description 针对表【user(User table)】的数据库操作Service
* @createDate 2023-12-15 23:32:27
*/
public interface UserService extends IService<User> {
    /**
     * Register a user
     *
     * @param userAccount User account
     * @param password  User password
     * @param checkPassword Check password
     * @param planetCode Code for the planet
     * @return User id
     */
    long userRegister(String userAccount, String password, String checkPassword, String planetCode);

    /**
     * Login a user
     *
     * @param userAccount User account
     * @param password User password
     * @return User info without sensitive information
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * Mask the user to remove sensitive data
     *
     * @param user
     * @return
     */
    User getMaskedUser(User user);

    /**
     * Logout a user
     *
     * @param request
     */
    int userLogout(HttpServletRequest request);
}
