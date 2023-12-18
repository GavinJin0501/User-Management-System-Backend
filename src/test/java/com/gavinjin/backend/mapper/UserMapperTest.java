package com.gavinjin.backend.mapper;
import java.util.Date;

import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * User service test
 */
@SpringBootTest
class UserMapperTest {
    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("test-jjy");
        user.setUserAccount("jjy-123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("1358555");
        user.setEmail("ahaha@jjy.edu");

        boolean result = userService.save(user);
        Assertions.assertTrue(result);
        Assertions.assertNotNull(user.getId());
    }
}