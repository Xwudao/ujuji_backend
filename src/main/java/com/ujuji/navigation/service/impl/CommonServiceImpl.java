package com.ujuji.navigation.service.impl;

import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.model.dto.ImportBoxDto;
import com.ujuji.navigation.model.dto.LinkDto;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.BoxService;
import com.ujuji.navigation.service.CommonService;
import com.ujuji.navigation.service.LinkService;
import com.ujuji.navigation.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Resource
    BoxService boxService;
    @Resource
    LinkService linkService;
    @Resource
    RedisUtils redisUtils;

    @Override
    public Integer importBoxAndLinks(ImportBoxDto importBoxDto) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        if (userInfo == null) {// 未登录
            throw new MyException(ResultCode.USER_NOT_LOGGED_IN);
        }

        Integer userId = userInfo.getId();
        String boxTitle = importBoxDto.getBoxTitle();

        BoxEntity byTitleAndUid = boxService.findByTitleAndUid(boxTitle, userId);

        if (byTitleAndUid != null) {// 盒子已存在
            throw new MyException(ResultCode.IMPORTED_BOX_HAS_EXISTED);
        }
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.setTitle(importBoxDto.getBoxTitle());
        boxEntity.setUserId(userId);
        boolean b = boxService.insertOne(boxEntity, userInfo);
        if (!b) {// 插入盒子失败，就不进行导入
            throw new MyException(ResultCode.INSERT_BOX_FAILED);
        }
        int result = 0;
        for (LinkDto link : importBoxDto.getLinks()) {
            if (!MyUtils.isLink(link.getLink())) continue;

            LinkEntity linkEntity = new LinkEntity();
            linkEntity.setBoxId(boxEntity.getId());
            linkEntity.setTitle(link.getTitle());
            linkEntity.setLink(link.getLink());

            boolean insert = linkService.insert(linkEntity, userInfo);
            if (insert) {
                result += 1;
            }
        }

        return result;
    }

    @Override
    public String getWeather(String city) {
        String apiUrl = Constants.Weather.API_URL;
        Object o = redisUtils.get(Constants.Weather.KEY_WEATHER + city);
        if (o != null) {
            return (String) o;
        }

        String url = null;
        try {
            url = apiUrl.replace("[city]", URLEncoder.encode(city, "utf-8"));
            String html = HttpUtils.getHtml(url);
            redisUtils.set(Constants.Weather.KEY_WEATHER + city, html, 10 * 60);
            return html;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
