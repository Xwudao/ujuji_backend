package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.SearchSiteMapper;
import com.ujuji.navigation.model.entity.SearchSiteEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.SearchSiteService;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SearchSiteServiceImpl implements SearchSiteService {

    @Resource
    SearchSiteMapper searchSiteMapper;

    @Override
    public List<SearchSiteEntity> findByUserId(Integer userId) {
        QueryWrapper<SearchSiteEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("site_order");
        return searchSiteMapper.selectList(wrapper);
    }

    @Override
    public SearchSiteEntity findById(Integer id) {
        try {
            return searchSiteMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertOne(SearchSiteEntity noticeEntity) {
        return searchSiteMapper.insert(noticeEntity) > 0;
    }

    @Override
    public boolean update(SearchSiteEntity searchSiteEntity, UserEntity userEntity) {
        final SearchSiteEntity byId = findById(searchSiteEntity.getId());
        if (byId == null) {
            throw new MyException(ResultCode.ITEM_NOT_EXISTS);
        }
        if (!byId.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }
        return searchSiteMapper.updateById(searchSiteEntity) > 0;
    }

    @Override
    public boolean deleteOneById(Integer id, UserEntity userEntity) {
        final SearchSiteEntity byId = findById(id);
        if (byId == null) {
            throw new MyException(ResultCode.ITEM_NOT_EXISTS);
        }
        if (!byId.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }
        return searchSiteMapper.deleteById(id) > 0;
    }
}
