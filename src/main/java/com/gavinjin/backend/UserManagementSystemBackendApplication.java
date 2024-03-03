package com.gavinjin.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.gavinjin.backend.mapper")
@EnableScheduling
public class UserManagementSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementSystemBackendApplication.class, args);
    }

}
