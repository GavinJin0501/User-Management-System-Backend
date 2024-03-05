package com.gavinjin.backend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedissonClient redissonClient;

    // Major users
    private List<Long> mainUserList = Arrays.asList(1L);

    // Execute daily
    @Scheduled(cron = "0 26 15 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("gavinjin:preCacheJob:doCache:lock");

        try {
            // Only one server will actually execute the scheduled job
            // Set auto release time to -1 to enable redisson WatchDog:
            // Listen the current thread, refill the lock to 30 seconds every 10 seconds if no unlock
            if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
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
        } catch (InterruptedException e) {
            log.error("redisson lock error", e);
        } finally {
            // release the lock when the job completes
            // can only unlock one's own lock
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
