package com.ujuji.navigation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.ReplyMsgDto;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.LeaveMsgService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("user/leaveMsg")

@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class LeaveMsgController {

    @Resource
    LeaveMsgService leaveMsgService;

    /*查询留言*/
    @GetMapping("/search")
    public AppResult<IPage<LeaveMsgEntity>> searchMsg(@RequestParam String kw, @RequestParam int pageNo,
                                                      @RequestParam int pageSize) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final IPage<LeaveMsgEntity> msgByUserIdWithNoFixed = leaveMsgService.searchMsg(kw, userInfo.getId(), pageSize,
                pageNo);
        return AppResultBuilder.success(msgByUserIdWithNoFixed, ResultCode.SUCCESS);
    }

    @GetMapping
    public AppResult<IPage<LeaveMsgEntity>> findByUserId(@RequestParam int pageSize, @RequestParam int pageNo) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final IPage<LeaveMsgEntity> byUserId = leaveMsgService.findByUserId(userInfo.getId(), pageSize, pageNo);
        return AppResultBuilder.success(byUserId, ResultCode.SUCCESS);
    }


    /*设置留言置顶*/
    @GetMapping("/setFixed/{id}")
    public AppResult<LeaveMsgEntity> setFixed(@PathVariable int id) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final boolean b = leaveMsgService.setFixed(id, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SET_MSG_FIXED_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SET_MSG_FIXED_FAIL);
    }

    /*设置留言已读*/
    @GetMapping("/setRead/{id}")
    public AppResult<LeaveMsgEntity> setRead(@PathVariable int id) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final boolean b = leaveMsgService.setRead(id, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SET_MSG_READ_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SET_MSG_READ_FAIL);
    }

    /*获取留言未读个数*/
    @GetMapping("/getNonReadCount")
    public AppResult<Integer> getNonReadCount() {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final Integer nonReadMsg = leaveMsgService.findNonReadMsg(userInfo.getId());
        return AppResultBuilder.success(nonReadMsg, ResultCode.SUCCESS);
    }


    /*修改*/
    @PutMapping
    public AppResult<LeaveMsgEntity> update(@RequestBody @Valid LeaveMsgEntity leaveMsgEntity) {
        final boolean b = leaveMsgService.updateOne(leaveMsgEntity);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_UPDATE_FAIL);
    }

    /*回复留言*/
    @PutMapping("/reply")
    public AppResult<LeaveMsgEntity> reply(@RequestBody @Valid ReplyMsgDto replyMsgDto) {

        LeaveMsgEntity leaveMsgEntity = new LeaveMsgEntity();
        leaveMsgEntity.setReply(replyMsgDto.getReply());
        leaveMsgEntity.setId(replyMsgDto.getId());
        final boolean b = leaveMsgService.updateOne(leaveMsgEntity);
        return b ? AppResultBuilder.successNoData(ResultCode.REPLY_MSG_SUCCESS) :
                AppResultBuilder.fail(ResultCode.REPLY_MSG_FAIL);
    }

    /*删除留言*/
    @DeleteMapping("/{id}")
    public AppResult<LeaveMsgEntity> deleteById(@PathVariable int id) {
        final UserEntity userInfo = AuthInfo.getUserInfo();
        final boolean b = leaveMsgService.deleteOne(id, userInfo);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.fail(ResultCode.SERVICE_DELETE_FAIL);
    }

}
