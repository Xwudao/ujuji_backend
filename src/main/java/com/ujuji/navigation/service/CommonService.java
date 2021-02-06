package com.ujuji.navigation.service;

import com.ujuji.navigation.model.dto.ImportBoxDto;

//专门处理包含多个其他service的操作
public interface CommonService {

    Integer importBoxAndLinks(ImportBoxDto importBoxDto);

    String getWeather(String city);

}
