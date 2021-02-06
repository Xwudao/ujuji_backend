package com.ujuji.navigation.util;

import com.ujuji.navigation.model.dto.VerifyCodeDto;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.lang3.RandomUtils;

public class VerifyCodeUtils {

    public static VerifyCodeDto getRandomCode() {
        int num = RandomUtils.nextInt(1, 100);
        int r = num % 2;
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto();

        switch (r) {
            // case 2:
            //     // 中文类型
            //     ChineseCaptcha chineseCaptcha = new ChineseCaptcha(120, 40, 3);
            //     verifyCodeDto.setCode(chineseCaptcha.text());
            //     verifyCodeDto.setImage(chineseCaptcha.toBase64());
            //     break;
            case 1:
                //png
                SpecCaptcha captcha = new SpecCaptcha(120, 40, 4);
                verifyCodeDto.setCode(captcha.text());
                verifyCodeDto.setImage(captcha.toBase64());
                break;
            case 0://算术
                ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(120, 40, 2);
                verifyCodeDto.setCode(arithmeticCaptcha.text());
                verifyCodeDto.setImage(arithmeticCaptcha.toBase64());
                break;
        }
        return verifyCodeDto;
    }
}
