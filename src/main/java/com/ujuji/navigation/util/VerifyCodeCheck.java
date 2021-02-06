package com.ujuji.navigation.util;

import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class VerifyCodeCheck {
    @Resource
    RedisUtils redisUtils;

    /**
     * 检查验证码
     *
     * @param verifyCode 用户输入的验证码
     * @param code       验证码的key
     * @throws MyException 验证失败异常
     */
    public void checkVerifyCode(String verifyCode, String code) throws MyException {
        if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(code)) {
            throw new MyException(ResultCode.PLEASE_VERIFY_CODE);
        }

        final String originCode = (String) redisUtils.get(Constants.Common.KEY_VERIFY_CODE + code);
        log.info("originCode ==>> {} ", originCode);

        if (originCode == null) {
            throw new MyException(ResultCode.VERIFY_CODE_EXPIRED);
        }
        if (!originCode.toLowerCase().equals(verifyCode.toLowerCase())) {
            throw new MyException(ResultCode.VERIFY_CODE_ERROR);
        }
        // 走到这一步说明验证码已经验证成功了，所以这一步 就是清除redis中的数据，同一个验证码不能一直用
        redisUtils.del(Constants.Common.KEY_VERIFY_CODE + code);
    }

    /**
     * 检测修改密码的code是否准确
     *
     * @param email 电子邮件
     * @param code  用户输入的验证码
     * @return 是否准确
     */
    public boolean checkPasswordCode(String email, String code) {
        Object o = redisUtils.get(Constants.Mail.KEY_FORGET_CODE_PREFIX + email);
        log.info("o ==>> {}", o);
        if (o != null) {
            String originCode = (String) o;
            log.info("checkPasswordCode ==>> code =>> {}", code);
            log.info("checkPasswordCode ==>> originCode =>> {}", originCode);
            boolean equals = originCode.equals(code);
            if (equals) {
                redisUtils.del(Constants.Mail.KEY_FORGET_CODE_PREFIX + email);//删除
                return true;
            }
        }
        return false;
    }
}
