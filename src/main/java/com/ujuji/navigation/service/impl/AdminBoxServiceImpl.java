package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.BoxMapper;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.service.AdminBoxService;
import com.ujuji.navigation.service.BoxService;
import com.ujuji.navigation.service.LinkService;
import com.ujuji.navigation.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminBoxServiceImpl implements AdminBoxService {
    @Resource
    BoxMapper boxMapper;

    @Override
    public IPage<BoxEntity> findAllByPage(int pageNum, int pageSize) {
        IPage<BoxEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return boxMapper.selectPage(page, wrapper);
    }

    @Resource
    LinkService linkService;
    @Resource
    BoxService boxService;

    @Override
    public IPage<BoxEntity> searchByPage(String key, int pageNum, int pageSize) {
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        IPage<BoxEntity> page = new Page<>(pageNum, pageSize);
        try {
            final Integer id = Integer.valueOf(key);
            wrapper.eq("id", id).or().like("title", key);
        } catch (NumberFormatException e) {
            wrapper.like("title", key);
        }
        return boxMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean deleteOne(Integer boxId) {
        final BoxEntity box = boxService.findById(boxId);
        linkService.deleteByBoxId(box.getUserId());//删除所属该盒子的所有链接
        return boxMapper.deleteById(boxId) > 0;
    }

    @Override
    public boolean updateOne(BoxEntity boxEntity) {
        if (StringUtils.isBlank(boxEntity.getId().toString()) || boxEntity.getId() == 0) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }

        return boxMapper.updateById(boxEntity) > 0;
    }
}
