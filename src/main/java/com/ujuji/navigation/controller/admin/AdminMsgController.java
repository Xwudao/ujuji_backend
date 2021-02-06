package com.ujuji.navigation.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;
import com.ujuji.navigation.service.AdminMsgService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("admin/msg")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminMsgController {

    @Resource
    AdminMsgService adminMsgService;


    //查询所有，分页
    @GetMapping
    public AppResult<IPage<LeaveMsgEntity>> findAllByPage(@RequestParam int page, @RequestParam int size) {
        if (size <= 0) {
            size = 20;
        }
        if (page <= 0) {
            page = 1;
        }
        IPage<LeaveMsgEntity> allByPage = adminMsgService.findByPage(page, size);
        return AppResultBuilder.success(allByPage, ResultCode.SUCCESS);
    }

    //更新
    @PutMapping
    public AppResult<LeaveMsgEntity> updateOne(@RequestBody LeaveMsgEntity leaveMsgEntity) {
        boolean b = adminMsgService.updateOneById(leaveMsgEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    //删除
    @DeleteMapping("/{msgId}")
    public AppResult<LeaveMsgEntity> findAllByPage(@PathVariable Integer msgId) {
        boolean b = adminMsgService.deleteOne(msgId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }


}
