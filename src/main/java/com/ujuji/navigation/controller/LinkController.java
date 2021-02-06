package com.ujuji.navigation.controller;

import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.LinkService;
import com.ujuji.navigation.service.UserService;
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
@RequestMapping("user/link")

@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class LinkController {
    @Resource(name = "userLinkService")
    LinkService linkService;
    @Resource
    UserService userService;

    @PostMapping
    public AppResult<LinkEntity> insert(@RequestBody @Valid LinkEntity linkEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boolean b = linkService.insert(linkEntity, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);
    }

    @PutMapping
    public AppResult<LinkEntity> update(@RequestBody LinkEntity linkEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boolean b = linkService.update(linkEntity, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @DeleteMapping("/{linkId}")
    public AppResult<LinkEntity> deleteOne(@PathVariable Integer linkId) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        boolean b = linkService.deleteById(linkId, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    @GetMapping("/box/{boxId}")
    public AppResult<List<LinkEntity>> findByBoxId(@PathVariable Integer boxId) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        List<LinkEntity> boxes = linkService.findAllByBoxId(boxId);
        return AppResultBuilder.success(boxes, ResultCode.SUCCESS);
    }

}
