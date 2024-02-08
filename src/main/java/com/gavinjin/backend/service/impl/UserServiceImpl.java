package com.gavinjin.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gavinjin.backend.common.StatusCode;
import com.gavinjin.backend.exception.BusinessException;
import com.gavinjin.backend.mapper.UserMapper;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        // 1. Check input
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword, planetCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Parameter is empty");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Account name is too short");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Password / check password is too short");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Planet code is too long");
        }

        // 2. User account can not contain special characters
        if (PATTERN.matcher(userAccount).find()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "User account can not contain special characters");
        }

        // 3. Password and checkPassword should be the same
        if (!password.equals(checkPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Password and check password is not the same");
        }

        // 4.1 Check no user with the same account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Account name is used");
        }

        // 4.2 Check no user with the same planet code
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Planet code is used");
        }

        // 5. Encrypt the password
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 6. Insert the user
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = save(user);
        if (!saveResult) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "Database fails to insert the user");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        // 1. Check input
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Parameters empty");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "User account is too long");
        }
        if (password.length() < 8) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "Password is too long");
        }

        // 2. User account can not contain special characters
        if (PATTERN.matcher(userAccount).find()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "User account can not contain special characters");
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
            throw new BusinessException(StatusCode.PARAMS_ERROR, "userAccount cannot match userPassword");
        }

        // 4. Save the masked user logged in status on the server (session)
        User maskedUser = getMaskedUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, maskedUser);
        return maskedUser;
    }

    @Override
    public User getMaskedUser(User user) {
        if (user == null) {
            return null;
        }

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
        maskedUser.setPlanetCode(user.getPlanetCode());
        maskedUser.setTags(user.getTags());
        return maskedUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Deprecated
    @Override
    public List<User> searchUsersByTagsBySQL(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(this::getMaskedUser).collect(Collectors.toList());
    }

    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return users.stream().filter(user -> {
            String tagsStr = user.getTags();
            // if (StringUtils.isAnyBlank(tagsStr)) {
            //     return false;
            // }
            Set<String> tempTagNameList = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            tempTagNameList = Optional.ofNullable(tempTagNameList).orElse(new HashSet<>());

            for (String tagName : tagNameList) {
                if (!tempTagNameList.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getMaskedUser).collect(Collectors.toList());
    }

}




