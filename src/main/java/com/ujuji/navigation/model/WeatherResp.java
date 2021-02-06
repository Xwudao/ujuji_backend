package com.ujuji.navigation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WeatherResp {

    /**
     * cityid : 101272004
     * city : 成都
     * update_time : 13:45
     * wea : 多云
     * wea_img : yun
     * tem : 27
     * tem_day : 28
     * tem_night : 22
     * win : 东风
     * win_speed : 2级
     * win_meter : 小于12km/h
     * air : 16
     */

    private String cityid;
    private String city;
    private String update_time;
    private String wea;
    private String wea_img;
    private String tem;
    private String tem_day;
    private String tem_night;
    private String win;
    private String win_speed;
    private String win_meter;
    private String air;
}
