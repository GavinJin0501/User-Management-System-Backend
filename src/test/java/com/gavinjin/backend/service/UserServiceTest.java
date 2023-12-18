package com.gavinjin.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testRegister() {
        String userAccount = "te st1";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertNotEquals(-1, l);
    }
}