package com.gavinjin.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gavinjin.backend.common.BaseResponse;
import com.gavinjin.backend.common.ResponseUtils;
import com.gavinjin.backend.common.StatusCode;
import com.gavinjin.backend.exception.BusinessException;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.model.request.UserLoginRequest;
import com.gavinjin.backend.model.request.UserRegisterRequest;
import com.gavinjin.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.gavinjin.backend.constant.UserConstant.ADMIN_ROLE;
import static com.gavinjin.backend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * User controller
 */
@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin()
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        long usedId = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResponseUtils.success(usedId);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }


        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResponseUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(StatusCode.NO_AUTH);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> resultList = userList.stream().map(user -> userService.getMaskedUser(user)).collect(Collectors.toList());
        return ResponseUtils.success(resultList);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        List<User> users = userService.searchUsersByTags(tagNameList);
        return ResponseUtils.success(users);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // check if the parameter is empty
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_AUTH);
        }
        int result = userService.updateUser(user, loginUser);
        return ResponseUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(StatusCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResponseUtils.success(b);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(StatusCode.NOT_LOGGED_IN);
        }

        // Update the user info if changed
        long userId = user.getId();
        // TODO: check if the user is valid
        user = userService.getById(userId);
        User maskedUser = userService.getMaskedUser(user);
        return ResponseUtils.success(maskedUser);
    }
}
