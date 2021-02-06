package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(fill = FieldFill.INSERT)//INSERT代表只在插入时填充
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//get
    protected LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)// INSERT_UPDATE 首次插入、其次更新时填充(或修改)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//get
    protected LocalDateTime updatedAt;
}
