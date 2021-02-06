package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportBoxDto {
    @NotBlank(message = "盒子标题必须存在")
    private String boxTitle;
    @NotEmpty(message = "待导入的链接（书签）个数必须大于等于1")
    private List<LinkDto> links;
}
