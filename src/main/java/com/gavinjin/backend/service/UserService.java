package com.gavinjin.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gavinjin.backend.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Deprecated
    List<User> searchUsersByTagsBySQL(List<String> tagNameList);

    /**
     * Search users based on tags
     *
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * Update the user
     *
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(User user, User loginUser);

    /**
     * Obtain the current user info
     *
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * Check if the current logged-in user is an administrator
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * Check if the current logged-in user is an administrator
     *
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);
}
