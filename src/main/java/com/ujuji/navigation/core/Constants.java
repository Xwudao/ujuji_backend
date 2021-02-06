package com.ujuji.navigation.core;

public class Constants {

    public interface REGEX {
        String LINK_REGEX = "^https?://.*";
    }

    public interface Weather {
        String API_URL =
                "https://www.baidu.com/home/other/data/weatherInfo?city=[city]&indextype=manht&asyn=1";
        String KEY_WEATHER = "key_weather::";
    }


    public interface Common {
        String KEY_VERIFY_CODE = "key_verify_code::";
        String KEY_MSG_INTERVAL = "key_msg_interval::";
        String KEY_FREQUENT_LIMIT = "key_frequent_limit::";

        String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit";

    }

    public interface Mail {
        String STRING_FORGET_MAIL_SUBJECT = "【优聚集】忘记密码重置邮件";
        String STRING_FORGET_MAIL_FROM = "优聚集-ujuji.com";
        String KEY_FORGET_CODE_PREFIX = "key_forgot_code_prefix::";
    }

    public interface CardCode {
        Integer CARD_CODE_LENGTH = 15;
    }

    public enum CardCodeType {
        RENAME
    }
}
