package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("global_settings")
public class GlobalSettingEntity extends BaseEntity {
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "内容不能为空")
    private String value;
}
