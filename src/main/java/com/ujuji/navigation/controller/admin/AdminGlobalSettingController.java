package com.ujuji.navigation.controller.admin;

import com.ujuji.navigation.model.entity.GlobalSettingEntity;
import com.ujuji.navigation.service.GlobalSettingService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/globalSetting")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminGlobalSettingController {

    @Resource
    GlobalSettingService globalSettingService;

    /*查询所有*/
    @GetMapping
    public AppResult<List<GlobalSettingEntity>> findAll() {
        return AppResultBuilder.success(globalSettingService.findAll(), ResultCode.SUCCESS);
    }

    @GetMapping("/{name}")
    public AppResult<GlobalSettingEntity> findByName(@PathVariable String name) {
        return AppResultBuilder.success(globalSettingService.findByName(name), ResultCode.SUCCESS);
    }


    @PostMapping
    public AppResult<GlobalSettingEntity> insertOne(@RequestBody @Valid GlobalSettingEntity globalSettingEntity) {
        final boolean b = globalSettingService.insertOne(globalSettingEntity);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);
    }


    @PutMapping
    public AppResult<GlobalSettingEntity> update(@RequestBody GlobalSettingEntity globalSettingEntity) {

        boolean b = globalSettingService.updateById(globalSettingEntity);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @DeleteMapping("/{settingId}")
    public AppResult<GlobalSettingEntity> deleteOne(@PathVariable Integer settingId) {
        boolean b = globalSettingService.deleteById(settingId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }


}
