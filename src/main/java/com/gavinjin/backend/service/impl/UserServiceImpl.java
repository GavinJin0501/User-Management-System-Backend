package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.mapper.UserMapper;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.gavinjin.backend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author gavin
* @description 针对表【user(User table)】的数据库操作Service实现
* @createDate 2023-12-15 23:32:27
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    private static final String SPECIAL_CHARACTERS = "[^a-zA-Z0-9_]";

    private static final Pattern PATTERN = Pattern.compile(SPECIAL_CHARACTERS);

    /**
     * Salt value to encrypt the password
     */
    private static final String SALT = "Gavin-Jin";

    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        // 1. Check input
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 2. User account can not contain special characters
        if (PATTERN.matcher(userAccount).find()) {
            return -1;
        }

        // 3. Password and checkPassword should be the same
        if (!password.equals(checkPassword)) {
            return -1;
        }

        // 4. Check no user with the same account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 5. Encrypt the password
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 6. Insert the user
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        boolean saveResult = save(user);
        if (!saveResult) {
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        // 1. Check input
        if (StringUtils.isAnyBlank(userAccount, password)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (password.length() < 8) {
            return null;
        }

        // 2. User account can not contain special characters
        if (PATTERN.matcher(userAccount).find()) {
            return null;
        }

        // 3. Check username and password
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptedPassword);
        User user = userMapper.selectOne(queryWrapper);
        // User does not exist
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        // 4. Save the masked user logged in status on the server (session)
        User maskedUser = getMaskedUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, maskedUser);
        return maskedUser;
    }

    /**
     * Mask the user to remove sensitive data
     *
     * @param user
     * @return
     */
    @Override
    public User getMaskedUser(User user) {
        User maskedUser = new User();
        maskedUser.setId(user.getId());
        maskedUser.setUsername(user.getUsername());
        maskedUser.setUserAccount(user.getUserAccount());
        maskedUser.setAvatarUrl(user.getAvatarUrl());
        maskedUser.setGender(user.getGender());
        maskedUser.setPhone(user.getPhone());
        maskedUser.setEmail(user.getEmail());
        maskedUser.setUserRole(user.getUserRole());
        maskedUser.setUserStatus(user.getUserStatus());
        maskedUser.setCreatedTime(user.getCreatedTime());
        return maskedUser;
    }
}




