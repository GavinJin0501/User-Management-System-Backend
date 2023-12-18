package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @return User id
     */
    long userRegister(String userAccount, String password, String checkPassword);
}
