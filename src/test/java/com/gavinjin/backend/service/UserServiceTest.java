package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testRegister() {
        String userAccount = "te st1";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "haha";
        long l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertNotEquals(-1, l);
    }

    @Test
    void searchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> users = userService.searchUsersByTags(tagNameList);
        Assertions.assertNotNull(users);
        System.out.println(users);
    }

    @Test
    void searchUsersByTagsBySQL() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> users = userService.searchUsersByTagsBySQL(tagNameList);
        Assertions.assertNotNull(users);
        System.out.println(users);
    }
}