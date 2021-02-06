package com.ujuji.navigation.controller;

import com.ujuji.navigation.annotation.FrequencyLimit;
import com.ujuji.navigation.model.entity.NoticeEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.NoticeService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("user/notice")

@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class NoticeController {
    @Resource
    NoticeService noticeService;

    @GetMapping
    public AppResult<NoticeEntity> findOne() {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        if (userInfo != null) {
            final NoticeEntity notice = noticeService.findByUserId(userInfo.getId());
            return AppResultBuilder.success(notice, ResultCode.SUCCESS);
        } else {
            return AppResultBuilder.fail(ResultCode.SERVICE_QUERY_FAIL);
        }
    }

    @PostMapping
    @FrequencyLimit
    public AppResult<NoticeEntity> insert(@RequestBody @Valid NoticeEntity noticeEntity) {
        boolean b = noticeService.insertOne(noticeEntity);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_INSERT_FAIL);
    }

    @PutMapping
    @FrequencyLimit
    public AppResult<NoticeEntity> update(@RequestBody @Valid NoticeEntity noticeEntity) {
        UserEntity userInfo = AuthInfo.getUserInfo();
        noticeEntity.setUserId(userInfo.getId());
        boolean b = noticeService.update(noticeEntity, userInfo.getId());
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }
}
