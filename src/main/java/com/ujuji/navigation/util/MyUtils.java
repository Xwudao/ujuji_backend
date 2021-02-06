package com.ujuji.navigation.util;

import com.ujuji.navigation.core.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {
    public static Boolean isLink(String link) {
        Pattern compile = Pattern.compile(Constants.REGEX.LINK_REGEX);
        Matcher matcher = compile.matcher(link);
        return matcher.find();
    }
}
