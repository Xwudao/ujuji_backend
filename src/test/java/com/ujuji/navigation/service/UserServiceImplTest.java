package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    UserService userService;
    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    void findByUsername() {
        UserEntity user = userService.findByUsername("无道");
        System.out.println("user = " + user);
    }

    @Test
    void findById() {
        UserEntity user = userService.findById(1);
        System.out.println("user = " + user);
    }

    @Test
    void register() {
        // UserEntity userEntity = new UserEntity("无道", passwordEncoder.encode("12345"), "99141818@qq.com", 1,
        //         "ROLE_ADMIN");
        // Boolean register = userService.register(userEntity);
        // System.out.println("register = " + register);
    }
}