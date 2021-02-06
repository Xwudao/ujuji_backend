package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    @NotBlank(message = "关键词不能为空")
    private String key;
    private int pageSize;
    private int pageNo;
}
