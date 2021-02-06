package com.ujuji.navigation.util;

import com.ujuji.navigation.model.entity.UserEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthInfo {

    public static UserEntity getUserInfo() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            return (UserEntity) context.getAuthentication().getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
