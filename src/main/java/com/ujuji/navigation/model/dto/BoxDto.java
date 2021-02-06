package com.ujuji.navigation.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ujuji.navigation.model.entity.BaseEntity;
import com.ujuji.navigation.model.entity.LinkEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@TableName("boxes")
public class BoxDto extends BaseEntity {
    public BoxDto() {
    }

    public BoxDto(@NotBlank(message = "标题不能为空") String title, String titleIcon, @Min(value = 1, message = "排序值只能1-100之间") @Max(value = 100, message = "排序值只能1-100之间") Integer boxOrder, String introduction, Boolean openToOther, List<LinkEntity> links, Integer userId) {
        this.title = title;
        this.titleIcon = titleIcon;
        this.boxOrder = boxOrder;
        this.introduction = introduction;
        this.openToOther = openToOther;
        this.links = links;
        this.userId = userId;
    }


    @NotBlank(message = "标题不能为空")
    private String title;
    private String titleIcon;
    @Min(value = 1, message = "排序值只能1-100之间")
    @Max(value = 100, message = "排序值只能1-100之间")
    private Integer boxOrder;

    private String introduction;

    private Boolean openToOther;

    @TableField(exist = false)
    List<LinkEntity> links;

    private Integer userId;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleIcon() {
        return titleIcon;
    }

    public void setTitleIcon(String titleIcon) {
        this.titleIcon = titleIcon;
    }

    public Integer getBoxOrder() {
        return boxOrder;
    }

    public void setBoxOrder(Integer boxOrder) {
        this.boxOrder = boxOrder;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Boolean getOpenToOther() {
        return openToOther;
    }

    public void setOpenToOther(Boolean openToOther) {
        this.openToOther = openToOther;
    }

    public List<LinkEntity> getLinks() {
        return links;
    }

    public void setLinks(List<LinkEntity> links) {
        this.links = links;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
