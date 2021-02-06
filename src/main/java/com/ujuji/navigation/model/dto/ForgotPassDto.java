package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassDto {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "邮箱地址不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
    @NotBlank(message = "验证码系统错误")
    private String code;
}
