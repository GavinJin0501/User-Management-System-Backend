package com.gavinjin.backend.once;

import com.gavinjin.backend.model.domain.User;
import com.gavinjin.backend.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class InsertUsers {

    @Resource
    private UserService userService;

    // CPU密集型：分配核心线程数 = CPU - 1
    // IO密集型：分配核心线程数可以大于CPU核数
    private ExecutorService executorService = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * Insert large number of users serially
     */
    // @Scheduled(initialDelay = 1000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsersSerially() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // final int INSERT_NUM = 10_000_000;
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("fake_username");
            user.setUserAccount("fake_account");
            user.setAvatarUrl("https://gavinjin0501.github.io/static/avatar4.png");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("123");
            user.setEmail("123@qq.com");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("1111");
            userList.add(user);
        }
        // Total 100000, batchSize 10000, takes 12011 ms
        // Use batch to speed up
        userService.saveBatch(userList, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * Insert large number of users concurrently
     */
    // @Scheduled(initialDelay = 1000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsersConcurrently() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // final int INSERT_NUM = 10_000_000;
        final int INSERT_NUM = 500000;
        final int GROUP_SIZE = 40;

        // Split into 10 groups
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < GROUP_SIZE; i++) {
            List<User> userList = new ArrayList<>();
            for (int j = 0; j < INSERT_NUM / GROUP_SIZE; j++) {
                User user = new User();
                user.setUsername("fake_username");
                user.setUserAccount("fake_account");
                user.setAvatarUrl("https://gavinjin0501.github.io/static/avatar4.png");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("123");
                user.setEmail("123@qq.com");
                user.setTags("[]");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("1111");
                userList.add(user);
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                userService.saveBatch(userList, 10000);
            }, executorService);
            futureList.add(future);
        }

        // Total 100000, batchSize 10000, takes 4150 ms
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
