package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("site_config")
public class SiteConfigEntity extends BaseEntity {

    @NotBlank(message = "站点名不能为空")
    private String siteName;
    @NotBlank(message = "站点描述不能为空")
    private String siteDesc;
    @Pattern(regexp = "^[a-zA-Z0-9]{4,23}$", message = "请填写站点标签,且只能为字母和数字，且4<=位数<=23")
    private String suffix;
    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的站点颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String siteColor;
    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String siteSubColor;
    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String descColor;
    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String boxTitleColor;
    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String listItemColor;

    @Pattern(regexp = "^#?[0-9a-zA-Z]{3,8}", message = "请输入正确的颜色配置，不要随意输入(该页面有多个地方都需要设置颜色[必须])")
    private String searchSiteColor;

    @Pattern(regexp = "(^rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\)$)|(^rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+)(,\\s*\\d+\\" +
            ".\\d+)*\\)$)", message = "请输入正确的[盒子]颜色配置，其格式为: rgba(x,x,x,0.x)")
    private String boxColor;

    private String backgroundMusic;

    private String backgroundEffect;//背景特效

    // @Pattern(regexp = "^(?:https?://)?([(\\w+).]+?)/?[\\w/]+\\w+(\\.png|\\.jpg)$", message = "目前只支持网络链接的图片哟，且只能以Png" +
    //         "或Jpg结尾")
    private String backgroundImage;

    private Integer otherImg;//师傅启用每日bing的背景图片

    private String mobileImg;//手机壁纸

    private Boolean hidePwdBox;//隐藏私密盒子
    private Boolean hideSiteIcon;//隐藏站点图标

    private Integer userId;
    private Integer isShow;
}
