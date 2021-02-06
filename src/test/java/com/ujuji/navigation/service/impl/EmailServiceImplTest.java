package com.ujuji.navigation.service.impl;

import com.ujuji.navigation.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class EmailServiceImplTest {
    @Resource
    EmailService emailService;

    @Test
    void sendForgotPassEmail() {
        // emailService.sendForgotPassEmail("991418182@qq.com", "你好，这是重置邮件");
    }
}