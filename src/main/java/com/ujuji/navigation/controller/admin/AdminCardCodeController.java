package com.ujuji.navigation.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.model.dto.InsertCardCodeDto;
import com.ujuji.navigation.model.dto.PageDto;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.CardCodeEntity;
import com.ujuji.navigation.service.CardCodeService;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/cardCode")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminCardCodeController {


    @Resource
    CardCodeService cardCodeService;

    @GetMapping("/types")
    public AppResult<Constants.CardCodeType[]> findTypes() {
        Constants.CardCodeType[] values = Constants.CardCodeType.values();

        return AppResultBuilder.success(values, ResultCode.SUCCESS);
    }

    //分页查询所有
    @PostMapping("/find")
    public AppResult<IPage<CardCodeEntity>> findAllByPage(@RequestBody PageDto pageDto) {
        if (pageDto.getPageSize() <= 0) {
            pageDto.setPageSize(20);
        }
        if (pageDto.getPageNo() <= 0) {
            pageDto.setPageNo(1);
        }
        IPage<CardCodeEntity> byPage = cardCodeService.findByPage(pageDto.getPageNo(), pageDto.getPageSize());
        return AppResultBuilder.success(byPage, ResultCode.SUCCESS);
    }

    //搜索
    @PostMapping("/search")
    public AppResult<IPage<CardCodeEntity>> search(@RequestBody @Valid SearchDto searchDto) {
        IPage<CardCodeEntity> search = cardCodeService.search(searchDto);
        return AppResultBuilder.success(search, ResultCode.SUCCESS);
    }

    @DeleteMapping("/{cardCodeId}")
    public AppResult<CardCodeEntity> findAllByPage(@PathVariable Integer cardCodeId) {
        boolean b = cardCodeService.deleteById(cardCodeId);
        return b ? AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_SUCCESS) :
                AppResultBuilder.successNoData(ResultCode.SERVICE_DELETE_FAIL);
    }

    //插入卡密
    @PostMapping
    public AppResult<Object> insertMany(@RequestBody @Valid InsertCardCodeDto cardCodeDto) {
        List<String> strings = cardCodeService.insertMany(cardCodeDto.getType(), cardCodeDto.getNum());
        return AppResultBuilder.success(strings, ResultCode.SUCCESS);
    }

}
