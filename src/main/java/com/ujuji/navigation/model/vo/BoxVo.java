package com.ujuji.navigation.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxVo {

    private Integer id;

    private String introduction;

    private String title;

    private String titleIcon;

    protected LocalDateTime updatedAt;
    protected LocalDateTime createdAt;

}
