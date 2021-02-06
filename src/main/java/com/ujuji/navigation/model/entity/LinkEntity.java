package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("links")
public class LinkEntity extends BaseEntity {

    @NotBlank(message = "链接不能为空")
    private String link;
    @NotBlank(message = "标题不能为空")
    private String title;
    private String titleIcon;
    private String description;
    private Boolean isShow;
    @NotNull(message = "Box Id 不能为空")
    private Integer boxId;
    private Integer linkOrder;
}
