package com.ujuji.navigation.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.service.AdminBoxService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("admin/box")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminBoxController {

    @Resource
    AdminBoxService adminBoxService;


    @GetMapping
    public AppResult<IPage<BoxEntity>> findAllByPage(@RequestParam int page, @RequestParam int size) {
        if (size <= 0) {
            size = 20;
        }
        if (page <= 0) {
            page = 1;
        }
        IPage<BoxEntity> allByPage = adminBoxService.findAllByPage(page, size);
        return AppResultBuilder.success(allByPage, ResultCode.SUCCESS);
    }

    @DeleteMapping("/{boxId}")
    public AppResult<BoxEntity> findAllByPage(@PathVariable Integer boxId) {
        boolean b = adminBoxService.deleteOne(boxId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    @PutMapping
    public AppResult<BoxEntity> updateOne(@RequestBody BoxEntity boxEntity) {
        boolean b = adminBoxService.updateOne(boxEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @PostMapping("/search")
    public IPage<BoxEntity> search(@RequestBody @Valid SearchDto searchDto) {
        if (searchDto.getPageNo() <= 0) {
            searchDto.setPageNo(1);
        }
        if (searchDto.getPageSize() <= 0) {
            searchDto.setPageSize(25);
        }
        return adminBoxService.searchByPage(searchDto.getKey(), searchDto.getPageNo(),
                searchDto.getPageSize());
    }
}
