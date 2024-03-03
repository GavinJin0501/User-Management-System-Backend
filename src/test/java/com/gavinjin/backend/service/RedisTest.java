package com.gavinjin.backend.service;

import com.gavinjin.backend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testCRUD() {
        User user = new User();
        user.setId(6L);
        user.setUsername("Yackidyyac");

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // Create
        valueOperations.set("jjyString", "dog");
        valueOperations.set("jjyInt", 1);
        valueOperations.set("jjyDouble", 2.0);
        valueOperations.set("jjyUser", user);

        // Read
        Object jjyString = valueOperations.get("jjyString");
        Assertions.assertEquals("dog", ((String) jjyString));
        Object jjyInt = valueOperations.get("jjyInt");
        Assertions.assertEquals(1, ((Integer) jjyInt));
        Object jjyDouble = valueOperations.get("jjyDouble");
        Assertions.assertEquals(2.0, ((Double) jjyDouble));
        Object jjyUser = valueOperations.get("jjyUser");
        System.out.println(jjyUser);
    }
}
