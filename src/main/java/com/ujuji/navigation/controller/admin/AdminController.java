package com.ujuji.navigation.controller.admin;

import com.ujuji.navigation.model.entity.SiteConfigEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.SiteConfigService;
import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    UserService userService;
    @Resource
    SiteConfigService siteConfigService;

    @GetMapping("/userInfo/{userId}")
    public AppResult<Object> findUserInfo(@PathVariable int userId) {
        final UserEntity user = userService.findById(userId);
        if (user != null) {
            final SiteConfigEntity siteConfigEntity = siteConfigService.findByUserId(userId);
            Map<String, Object> info = new HashMap<>();
            info.put("user", user);
            info.put("config", siteConfigEntity);
            return AppResultBuilder.success(info, ResultCode.SUCCESS);
        } else {
            return AppResultBuilder.fail(ResultCode.USER_NOT_EXIST);
        }
    }
}
