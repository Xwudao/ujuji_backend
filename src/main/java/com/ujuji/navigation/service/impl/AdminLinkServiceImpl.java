package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.LinkMapper;
import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.service.AdminLinkService;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminLinkServiceImpl implements AdminLinkService {
    @Resource
    LinkMapper linkMapper;

    @Override
    public IPage<LinkEntity> findAllByPage(int pageNum, int pageSize) {
        IPage<LinkEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return linkMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean deleteOne(Integer linkId) {
        return linkMapper.deleteById(linkId) > 0;
    }

    @Override
    public IPage<LinkEntity> searchByPage(String key, int pageNum, int pageSize) {
        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        IPage<LinkEntity> page = new Page<>(pageNum, pageSize);
        try {
            final Integer id = Integer.valueOf(key);
            wrapper.eq("id", id).or().like("link", key).or().like("title", key).or().like("description", key);
        } catch (NumberFormatException e) {
            wrapper.like("link", key).or().like("title", key).or().like("description", key);
        }
        return linkMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean updateOne(LinkEntity linkEntity) {
        if (linkEntity.getId() == null) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }
        return linkMapper.updateById(linkEntity) > 0;
    }
}
