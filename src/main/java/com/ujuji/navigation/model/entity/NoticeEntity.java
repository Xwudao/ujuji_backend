package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("notices")
@AllArgsConstructor
@NoArgsConstructor
public class NoticeEntity extends BaseEntity {

    @NotNull(message = "所属用户id不能为空")
    private Integer userId;
    private String content;// 短的，一句话提示
    private String longNotice;// 长的公告

    private Integer isShow;

}
