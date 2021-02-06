package com.ujuji.navigation.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.service.AdminLinkService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("admin/link")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminLinkController {

    @Resource
    AdminLinkService adminLinkService;


    @GetMapping
    public AppResult<IPage<LinkEntity>> findAllByPage(@RequestParam int page, @RequestParam int size) {
        if (size <= 0) {
            size = 20;
        }
        if (page <= 0) {
            page = 1;
        }
        IPage<LinkEntity> allByPage = adminLinkService.findAllByPage(page, size);
        return AppResultBuilder.success(allByPage, ResultCode.SUCCESS);
    }

    @DeleteMapping("/{linkId}")
    public AppResult<LinkEntity> findAllByPage(@PathVariable Integer linkId) {
        boolean b = adminLinkService.deleteOne(linkId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    @PutMapping
    public AppResult<LinkEntity> updateOne(@RequestBody LinkEntity boxEntity) {
        boolean b = adminLinkService.updateOne(boxEntity);

        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_UPDATE_FAIL);
    }

    @PostMapping("/search")
    public IPage<LinkEntity> search(@RequestBody @Valid SearchDto searchDto) {
        if (searchDto.getPageNo() <= 0) {
            searchDto.setPageNo(1);
        }
        if (searchDto.getPageSize() <= 0) {
            searchDto.setPageSize(25);
        }
        return adminLinkService.searchByPage(searchDto.getKey(), searchDto.getPageNo(),
                searchDto.getPageSize());
    }
}
