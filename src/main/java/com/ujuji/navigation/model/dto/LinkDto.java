package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    @NotBlank(message = "链接不能为空")
    @Pattern(regexp = "^https?://.*",message = "书签必须是链接")
    private String link;
    @NotBlank(message = "标题不能为空")
    private String title;
    private String titleIcon;
    private String description;
}
