package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("leave_msg")
public class LeaveMsgEntity extends BaseEntity {
    private Integer userId;
    private Integer fixed;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotBlank(message = "内容不能为空")
    private String content;
    private Boolean isRead;
    private String reply;
    private String ip;
}
