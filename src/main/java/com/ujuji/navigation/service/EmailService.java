package com.ujuji.navigation.service;

import com.ujuji.navigation.model.dto.ForgotPassDto;

public interface EmailService {

    /**
     * 先检查是否已经发送过了
     *
     * @param forgotPassDto forgotPassDto
     */
    boolean checkHasSent(ForgotPassDto forgotPassDto);

    /**
     * 发送重置密码的邮件
     *
     * @param email 待发送的邮箱地址
     */
    void sendForgotPassEmail(String email);
}
