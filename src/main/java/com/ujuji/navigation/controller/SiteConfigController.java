package com.ujuji.navigation.controller;

import com.ujuji.navigation.annotation.FrequencyLimit;
import com.ujuji.navigation.model.entity.SiteConfigEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.SiteConfigService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("user/siteConfig")

@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class SiteConfigController {

    @Resource
    SiteConfigService siteConfigService;

    @PostMapping
    public AppResult<SiteConfigEntity> insertOne(@RequestBody @Valid SiteConfigEntity siteConfigEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        siteConfigEntity.setUserId(userInfo.getId());
        boolean insertOne = siteConfigService.insertOne(siteConfigEntity, userInfo);
        return insertOne ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);
    }

    @PutMapping
    @FrequencyLimit(10)
    public AppResult<SiteConfigEntity> updateOne(@RequestBody @Valid SiteConfigEntity siteConfigEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        siteConfigEntity.setUserId(userInfo.getId());
        boolean b = siteConfigService.updateOne(siteConfigEntity, userInfo);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);

    }

    @GetMapping("/suffix/available/{suffix}")
    public AppResult<Boolean> available(@PathVariable String suffix) {
        boolean b = siteConfigService.suffixAvailable(suffix);
        return AppResultBuilder.success(b, ResultCode.SUCCESS);
    }

}
