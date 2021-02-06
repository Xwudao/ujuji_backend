package com.ujuji.navigation.service.impl;

import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.model.dto.ForgotPassDto;
import com.ujuji.navigation.service.EmailService;
import com.ujuji.navigation.util.RedisUtils;
import com.ujuji.navigation.util.ResultCode;
import com.ujuji.navigation.util.VerifyCodeCheck;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Resource
    RedisUtils redisUtils;
    @Resource
    VerifyCodeCheck verifyCodeCheck;

    @Override
    public boolean checkHasSent(ForgotPassDto forgotPassDto) {

        //0 先检测图片验证码是否准确
        verifyCodeCheck.checkVerifyCode(forgotPassDto.getVerifyCode(), forgotPassDto.getCode());

        //1 先判断是否已经发送过了
        Object o = redisUtils.get(Constants.Mail.KEY_FORGET_CODE_PREFIX + forgotPassDto.getEmail());
        if (o != null) {
            throw new MyException(ResultCode.SEND_FORGOT_PASS_TIME_TOO_SHORT);
        }
        return true;
        // sendForgotPassEmail(forgotPassDto.getEmail());//这里再调用真实发送的方法
    }

    @Override
    @Async
    public void sendForgotPassEmail(String email) {
        //这里返回void的话，就不能捕获抛出的一次了

        //2 如果没发送过，或者已经过期，那么再次发送

        String random = RandomStringUtils.random(10, true, false);

        redisUtils.set(Constants.Mail.KEY_FORGET_CODE_PREFIX + email, random, 10 * 60);//10min
        String content = email + "：您正在找回密码，请输入下面的验证码：\n";
        content += "【" + random + "】" + "\n如果发送错误，请忽略此验证码\n【验证码10分钟内有效】";

        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setTo(email);
        simpleMessage.setSubject(Constants.Mail.STRING_FORGET_MAIL_SUBJECT);
        simpleMessage.setFrom(from);
        simpleMessage.setText(content);
        javaMailSender.send(simpleMessage);
    }
}
