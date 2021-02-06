package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveMsgDto {
    //    /*
//
//                     content: '',
//                     nickname: '',
//                     userId: -1,
//                     verifyCode: '',
//                     code: -1,*/
    private String ip;
    @NotBlank(message = "内容不能为空")
    private String content;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
    @NotNull(message = "用户ID不能为空")
    private Integer userId;
    @NotBlank(message = "验证码系统错误")
    private String code;

}
