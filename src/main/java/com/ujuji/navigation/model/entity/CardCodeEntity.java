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
@TableName("card_code")
//卡密系统
public class CardCodeEntity extends BaseEntity {

    @NotBlank(message = "卡密内容不能为空")
    private String content;
    private Boolean used;
    @NotBlank(message = "卡密类型不能为空")
    private String type;
    private Integer usedBy;//由哪个用户使用

}
