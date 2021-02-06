package com.ujuji.navigation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMsgDto {

    @NotNull(message = "留言ID不能为空")
    private Integer id;

    @NotBlank(message = "回复内容不能为空")
    private String reply;
}
