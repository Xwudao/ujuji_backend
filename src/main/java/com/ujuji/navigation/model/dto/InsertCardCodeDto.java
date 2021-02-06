package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCardCodeDto {

    @NotNull(message = "数量必须有")
    private Integer num=1;
    @NotBlank(message = "卡密类型不能为空")
    private String type;
}
