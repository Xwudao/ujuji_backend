package com.ujuji.navigation.controller;

import com.ujuji.navigation.model.dto.ImportBoxDto;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.BoxService;
import com.ujuji.navigation.service.CommonService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user/box")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class BoxController {

    @Resource(name = "userBoxService")
    BoxService boxService;
    @Resource
    CommonService commonService;

    @GetMapping("/user/{userId}")
    public AppResult<List<BoxEntity>> findByUserId(@PathVariable Integer userId) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        assert userInfo != null;
        List<BoxEntity> boxes = boxService.findByUserId(userInfo.getId());
        return AppResultBuilder.success(boxes, ResultCode.SUCCESS);
    }

    @PostMapping
    public AppResult<BoxEntity> insert(@RequestBody @Valid BoxEntity boxEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boxEntity.setUserId(userInfo.getId());//设置id
        boolean b = boxService.insertOne(boxEntity, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);
    }

    @PutMapping
    public AppResult<BoxEntity> update(@RequestBody BoxEntity boxEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boxEntity.setUserId(userInfo.getId());//设置id
        boolean b = boxService.updateById(boxEntity, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @DeleteMapping("/{boxId}")
    public AppResult<BoxEntity> deleteOne(@PathVariable Integer boxId) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boolean b = boxService.deleteById(boxId, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    @PostMapping("/import")
    public AppResult<Object> importBox(@RequestBody @Valid ImportBoxDto importBoxDto) {
        Integer integer = commonService.importBoxAndLinks(importBoxDto);
        return integer > 0 ? AppResultBuilder.success(integer, ResultCode.SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.IMPORT_FAILED);
    }

}
