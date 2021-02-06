package com.ujuji.navigation.controller;

import com.ujuji.navigation.model.entity.SearchSiteEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.SearchSiteService;
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
@RequestMapping("user/searchSite")

@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class SearchSiteController {

    @Resource
    SearchSiteService searchSiteService;

    /*查询所属用户的所有*/
    @GetMapping
    public AppResult<List<SearchSiteEntity>> findAll() {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final List<SearchSiteEntity> siteServiceByUserId = searchSiteService.findByUserId(userInfo.getId());
        return AppResultBuilder.success(siteServiceByUserId, ResultCode.SUCCESS);
    }

    /*插入*/
    @PostMapping
    public AppResult<Object> insert(@RequestBody @Valid SearchSiteEntity searchSiteEntity) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        searchSiteEntity.setUserId(userInfo.getId());
        final boolean b = searchSiteService.insertOne(searchSiteEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_INSERT_FAIL);
    }

    /*删除留言*/
    @DeleteMapping("/{id}")
    public AppResult<SearchSiteEntity> deleteById(@PathVariable int id) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final boolean b = searchSiteService.deleteOneById(id, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_DELETE_FAIL);
    }


    /*修改*/
    @PutMapping
    public AppResult<SearchSiteEntity> update(@RequestBody @Valid SearchSiteEntity searchSiteEntity) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final boolean b = searchSiteService.update(searchSiteEntity, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_UPDATE_FAIL);
    }

}
