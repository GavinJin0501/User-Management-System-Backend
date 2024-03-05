package com.gavinjin.backend.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        // list
        RList<String> rList = redissonClient.getList("test-list");
        rList.add("Gavin Jin");
        System.out.println("rList: " + rList.get(0));
        rList.remove(0);

        // map
        RMap<String, String> rMap = redissonClient.getMap("test-map");
    }

}
