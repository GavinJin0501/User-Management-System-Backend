package com.gavinjin.backend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavinjin.backend.mapper.UserMapper;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Pre setting cache
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    // Major users
    private List<Long> mainUserList = Arrays.asList(1L);

    // Execute daily
    @Scheduled(cron = "0 26 15 * * *") 
    public void doCacheRecommendUser() {
        for (Long userId : mainUserList) {
            String redisKey = String.format("gavinjin:user:recommend:%s", userId);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1 , 20), queryWrapper);

            try {
                valueOperations.set(redisKey, userPage, 30 , TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("redis set key error", e);
            }
        }
    }
}
