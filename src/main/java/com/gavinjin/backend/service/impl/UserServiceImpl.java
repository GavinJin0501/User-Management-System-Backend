package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import com.gavinjin.backend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author gavin
* @description 针对表【user(User table)】的数据库操作Service实现
* @createDate 2023-12-15 23:32:27
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    private static final String SPECIAL_CHARACTERS = "[^a-zA-Z0-9_]";

    private static final Pattern PATTERN = Pattern.compile(SPECIAL_CHARACTERS);

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
        final String SALT = "Gavin-Jin";
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
}




