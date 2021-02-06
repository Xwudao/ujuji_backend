package com.ujuji.navigation.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.AdminUserService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("admin/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminUserController {

    @Resource
    AdminUserService adminUserService;

    @GetMapping
    public AppResult<IPage<UserEntity>> findAllByPage(@RequestParam int page, @RequestParam int size) {
        if (size <= 0) {
            size = 20;
        }
        if (page <= 0) {
            page = 1;
        }
        IPage<UserEntity> allByPage = adminUserService.findAllByPage(page, size);
        return AppResultBuilder.success(allByPage, ResultCode.SUCCESS);
    }

    @DeleteMapping("/{userId}")
    public AppResult<UserEntity> findAllByPage(@PathVariable Integer userId) {
        boolean b = adminUserService.deleteOne(userId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    @PutMapping
    public AppResult<UserEntity> updateOne(@RequestBody UserEntity userEntity) {
        boolean b = adminUserService.updateOne(userEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @PostMapping("/search")
    public IPage<UserEntity> search(@RequestBody @Valid SearchDto searchDto) {
        if (searchDto.getPageNo() <= 0) {
            searchDto.setPageNo(1);
        }
        if (searchDto.getPageSize() <= 0) {
            searchDto.setPageSize(25);
        }
        return adminUserService.searchByPage(searchDto.getKey(), searchDto.getPageNo(),
                searchDto.getPageSize());
    }
}
