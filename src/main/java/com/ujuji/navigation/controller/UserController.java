package com.ujuji.navigation.controller;

import com.ujuji.navigation.model.dto.ForgotPassDto;
import com.ujuji.navigation.model.dto.UpdatePassWithCodeDto;
import com.ujuji.navigation.model.dto.UserDto;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.model.vo.UserVo;
import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController

@Slf4j
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("auth/register")
    public AppResult<UserEntity> register(@RequestBody @Valid UserDto userDto) {
        // userEntity.setAuthority(null);// 不能从前台获取数据
        Boolean register = userService.register(userDto);
        return register ? AppResultBuilder.successNoData(ResultCode.USER_REGISTER_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.USER_REGISTER_FAIL);
    }

    @GetMapping("/auth/info")
    public AppResult<Object> userInfo() {
        UserEntity userInfo = AuthInfo.getUserInfo();
        UserVo userVo = new UserVo();
        assert userInfo != null;
        BeanUtils.copyProperties(userInfo, userVo);
        return AppResultBuilder.success(userVo, ResultCode.SUCCESS);
    }

    // 发送修改密码的邮箱验证
    @PostMapping("auth/sendUpdateMail")
    public AppResult<Object> sendUpdateMail(@RequestBody @Valid ForgotPassDto forgotPassDto) {
        boolean b = userService.sendForgotPasswordEmail(forgotPassDto);
        return AppResultBuilder.success(b, ResultCode.FIND_PASS_EMAIL_SEND_SUCCESS);
    }

    //修改密码
    @PostMapping("auth/updatePassWithCode")
    public AppResult<Object> updatePassWithCode(@RequestBody @Valid UpdatePassWithCodeDto updatePassWithCodeDto) {
        log.info("updatePassWithCode ==>> dto ==>> {}", updatePassWithCodeDto.toString());
        boolean b = userService.updatePassWithCode(updatePassWithCodeDto);
        return b ? AppResultBuilder.successNoData(ResultCode.USER_PASSWORD_MODIFY_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.USER_PASSWORD_MODIFY_FAIL);
    }

    @PostMapping("/auth/token")
    public AppResult<Object> genToken() {
        String token = userService.generateAccessToken();
        log.info("gen token: {}", token);
        UserEntity userInfo = AuthInfo.getUserInfo();
        boolean b = userService.updateToken(userInfo, token);
        return b ? AppResultBuilder.success(token, ResultCode.GENERATE_TOKEN_SUCCESS) :
                AppResultBuilder.fail(ResultCode.GENERATE_TOKEN_FAIL);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("user/updatePassword")
    public AppResult<UserEntity> updatePassword(@RequestParam String oldPass, @RequestParam String newPass) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        return userService.updatePassword(oldPass, newPass, userInfo) ?
                AppResultBuilder.successNoData(ResultCode.USER_PASSWORD_MODIFY_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.USER_PASSWORD_MODIFY_FAIL);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("user/backup")
    public AppResult<List<BoxEntity>> backup() {
        UserEntity userInfo = AuthInfo.getUserInfo();
        final List<BoxEntity> boxesBackup = userService.findBoxesBackup(userInfo);
        return AppResultBuilder.success(boxesBackup, ResultCode.SUCCESS);
    }

}