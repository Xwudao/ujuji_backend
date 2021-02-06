package com.ujuji.navigation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.annotation.FrequencyLimit;
import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.model.dto.BoxDto;
import com.ujuji.navigation.model.dto.LeaveMsgDto;
import com.ujuji.navigation.model.dto.SearchLinkDto;
import com.ujuji.navigation.model.dto.VerifyCodeDto;
import com.ujuji.navigation.model.entity.*;
import com.ujuji.navigation.model.vo.BoxVo;
import com.ujuji.navigation.service.*;
import com.ujuji.navigation.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@Slf4j
/*此路径用来存放不需要权限的路径*/
public class PublicController {


    @Resource
    UserService userService;
    @Resource(name = "userBoxService")
    BoxService boxService;
    @Resource(name = "userLinkService")
    LinkService linkService;
    @Resource()
    SiteConfigService siteConfigService;
    @Resource
    NoticeService noticeService;
    @Resource
    RedisUtils redisUtils;

    //验证码
    @GetMapping("/verifyCode")
    public AppResult<Object> getVerifyCode() {
        try {
            VerifyCodeDto randomCode = VerifyCodeUtils.getRandomCode();
            String result = randomCode.getCode();
            String img = randomCode.getImage();
            String key_suffix = RandomStringUtils.random(10, true, true);
            log.info("verifyCode, key_suffix: {} ==>> result: {}", key_suffix, result);
            final VerifyCodeDto verifyCodeDto = new VerifyCodeDto(key_suffix, img);
            redisUtils.set(Constants.Common.KEY_VERIFY_CODE + key_suffix, result, 1000);//存储
            return AppResultBuilder.success(verifyCodeDto, ResultCode.SUCCESS);

            // final String s = VerifyCodeUtils.imageToBase64(120, 40, stringRandom);
            // String l = RandomStringUtils.random(10, true, true);
            // redisUtils.set(Constants.Common.KEY_VERIFY_CODE + l, stringRandom, 1000);//存储
            // final VerifyCodeDto verifyCodeDto = new VerifyCodeDto(l, s);
            // log.info("Key: {}, code: {}", l, stringRandom);
            // return AppResultBuilder.success(verifyCodeDto, ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResultBuilder.fail(ResultCode.GENERATE_VERIFY_CODE_FAIL);
        }
    }

    /*返回所有盒子，及其包括的链接*/
    @GetMapping("/allBoxes/{userId}")

    public AppResult<List<Map<String, Object>>> findByUserId(@PathVariable Integer userId) {
        UserEntity user = userService.findById(userId);
        if (user == null) {
            throw new MyException(ResultCode.BOX_NOT_FOUND);
        }
        List<BoxEntity> boxes = boxService.findByUserId(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        boxes.forEach((item) -> {
            List<LinkEntity> allByBoxId = linkService.findAllByBoxId(item.getId());
            Map<String, Object> map2 = new HashMap<>();
            map2.put("box", item);
            map2.put("links", allByBoxId);
            // map.put(boxes.indexOf(item), map2);
            list.add(map2);
        });
        return AppResultBuilder.success(list, ResultCode.SUCCESS);
    }

    @GetMapping("/allBoxes/token/{token}")
    public AppResult<Object> findBoxesByToken(@PathVariable String token) {
        UserEntity userEntity = userService.findUserByToken(token);
        if (userEntity == null) {
            return AppResultBuilder.fail(ResultCode.ACCESS_TOKEN_ERROR);
        }

        List<BoxEntity> boxes = boxService.findByUserId(userEntity.getId());
        List<BoxVo> boxVos = MyBeanUtils.copyListProperties(boxes, BoxVo::new);

        return AppResultBuilder.success(boxVos, ResultCode.SUCCESS);

    }
    /*根据用户id获取config*/
    @GetMapping("/siteConfig/{userId}")
    public AppResult<SiteConfigEntity> findSiteConfigByUserId(@PathVariable Integer userId) {
        SiteConfigEntity byUserId = siteConfigService.findByUserId(userId);
        if (byUserId != null) {
            return AppResultBuilder.success(byUserId, ResultCode.SUCCESS);
        }
        return AppResultBuilder.fail(ResultCode.SERVICE_QUERY_FAIL);
    }

    @Resource
    GlobalSettingService globalSettingService;

    @GetMapping("/system/info")
    public AppResult<Object> getInfo() {
        Map<String, Object> res = new HashMap<>();
        // 系统配置信息
        final List<GlobalSettingEntity> settings = globalSettingService.findAll();
        res.put("settings", settings);
        // final GlobalSettingEntity defaultShow = globalSettingService.findByName("defaultShow");
        // if (defaultShow != null) {
        //     final UserEntity userEntity = userService.findBoxesAndLinksBySuffix(defaultShow.getValue());
        // }

        return AppResultBuilder.success(res, ResultCode.SUCCESS);
    }

    @GetMapping("/nav/info/{suffix}")
    public AppResult<List<BoxEntity>> getNavInfo(@PathVariable String suffix) {
        final List<BoxEntity> boxesAndLinksBySuffix = userService.findBoxesAndLinksBySuffix(suffix);
        return AppResultBuilder.success(boxesAndLinksBySuffix, ResultCode.SUCCESS);
    }

    @GetMapping("/nav/pwd/{boxId}/{pwd}")
    public AppResult<BoxEntity> getNavInfoByPwd(@PathVariable Integer boxId, @PathVariable String pwd) {
        final BoxEntity boxEntity = boxService.findByPwd(boxId, pwd);
        return AppResultBuilder.success(boxEntity, ResultCode.SUCCESS);
    }

    @Resource
    SearchSiteService searchSiteService;

    @GetMapping("/config/info/{suffix}")
    public AppResult<Map<String, Object>> getConfigInfo(@PathVariable String suffix) {
        final SiteConfigEntity siteConfigEntity = siteConfigService.findBySuffix(suffix);
        final NoticeEntity noticeEntity = noticeService.findByUserId(siteConfigEntity.getUserId());
        final List<SearchSiteEntity> searchSite = searchSiteService.findByUserId(siteConfigEntity.getUserId());
        Map<String, Object> info = new HashMap<>();
        info.put("config", siteConfigEntity);
        info.put("searchSite", searchSite);
        info.put("notice", noticeEntity);

        return AppResultBuilder.success(info, ResultCode.SUCCESS);
    }

    @GetMapping("/search")
    public AppResult<Object> searchLink(@RequestBody SearchLinkDto searchLinkDto) {
        final List<LinkEntity> search = linkService.search(searchLinkDto.getKeyword());
        return AppResultBuilder.success(search, ResultCode.SUCCESS);
    }

    //公开接口，查询某个站点所有盒子信息
    @GetMapping("/boxes/{userId}")
    public AppResult<Object> getCategoriesByUserId(@PathVariable int userId) {
        List<BoxEntity> boxEntities = boxService.findByUserIdWithoutPwd(userId);
        List<BoxDto> boxDtos = MyBeanUtils.copyListProperties(boxEntities, BoxDto::new);
        return AppResultBuilder.success(boxDtos, ResultCode.SUCCESS);
    }

    //通过token添加站点链接
    @PostMapping("/token/{token}")
    public AppResult<Object> insertByToken(@PathVariable String token, @RequestBody @Valid LinkEntity linkEntity) {
        UserEntity userEntity = userService.findUserByToken(token);
        if (userEntity == null) {
            return AppResultBuilder.fail(ResultCode.ACCESS_TOKEN_ERROR);
        }

        boolean b = linkService.insert(linkEntity, userEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);

    }

//    ==============留言================

    @Resource
    LeaveMsgService leaveMsgService;

    /*查询留言*/
    @GetMapping("/msg/{userId}")
    public AppResult<IPage<LeaveMsgEntity>> findByPage(@PathVariable int userId, @RequestParam int pageNo,
                                                       @RequestParam int pageSize) {
        final IPage<LeaveMsgEntity> msgByUserIdWithNoFixed = leaveMsgService.findMsgByUserIdWithNoFixed(userId, pageSize, pageNo);
        return AppResultBuilder.success(msgByUserIdWithNoFixed, ResultCode.SUCCESS);
    }

    /*查询置顶留言*/
    @GetMapping("/msg/fixed/{userId}")
    public AppResult<List<LeaveMsgEntity>> findByPageFixed(@PathVariable int userId) {
        final List<LeaveMsgEntity> fixedByUserId = leaveMsgService.findFixedByUserId(userId);
        return AppResultBuilder.success(fixedByUserId, ResultCode.SUCCESS);
    }

    /*添加留言置顶*/
    @PostMapping("/msg")
    @FrequencyLimit(20)
    public AppResult<LeaveMsgEntity> insert(@RequestBody @Valid LeaveMsgDto leaveMsgDto, HttpServletRequest request) {

        final String ip = request.getRemoteAddr();

        leaveMsgDto.setIp(ip);
        final boolean b = leaveMsgService.insertOne(leaveMsgDto);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_INSERT_FAIL);
    }


    //    common service
    @Resource
    CommonService commonService;

    @GetMapping("weather")
    public AppResult<Object> getWeather(@RequestParam String city) {
        String weather = commonService.getWeather(city);
        return AppResultBuilder.success(weather, ResultCode.SUCCESS);
    }


}
