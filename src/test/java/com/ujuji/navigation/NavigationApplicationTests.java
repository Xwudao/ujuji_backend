package com.ujuji.navigation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class NavigationApplicationTests {

    public static void main(String[] args) {
        //$2a$10$xTOc1xrGt13w5pSF2wbOQO9kO4sVKfq6xfmTr4/lAaOSp5dOEMyee
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    @Resource
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
