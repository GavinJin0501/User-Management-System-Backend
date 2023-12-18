package com.gavinjin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserManagementSystemBackendApplicationTests {

    @Test
    void testDigest() throws NoSuchAlgorithmException {
        String salt = "abcd";
        String password = "jjy123";
        String encryptedPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        System.out.println(encryptedPassword);
    }
    @Test
    void contextLoads() {
    }

}
