package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.SiteConfigMapper;
import com.ujuji.navigation.model.entity.SiteConfigEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.SiteConfigService;
import com.ujuji.navigation.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SiteConfigServiceImpl implements SiteConfigService {
    @Resource
    SiteConfigMapper siteConfigMapper;

    @Override
    public SiteConfigEntity findById(Integer id) {

        QueryWrapper<SiteConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        try {
            return siteConfigMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SiteConfigEntity findBySuffix(String suffix) {
        QueryWrapper<SiteConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("suffix", suffix);
        try {
            return siteConfigMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCode.SELECT_ONE_FAIL);
        }
    }

    @Override
    public SiteConfigEntity findByUserId(Integer userId) {
        QueryWrapper<SiteConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        try {
            return siteConfigMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCode.SELECT_ONE_FAIL);
        }
    }

    @Override
    public boolean insertOne(SiteConfigEntity siteConfigEntity, UserEntity userEntity) {
        return siteConfigMapper.insert(siteConfigEntity) > 0;
    }

    @Override
    public boolean updateOne(SiteConfigEntity siteConfigEntity, UserEntity userEntity) {
        SiteConfigEntity site = findByUserId(userEntity.getId());
        if (site == null) {
            //等于空的话，就新建一个，不然就更新
            return insertOne(siteConfigEntity, userEntity);
        }


        siteConfigEntity.setId(site.getId());

        if (StringUtils.isNoneBlank(site.getSuffix())) {
            siteConfigEntity.setSuffix(null);
        } else {
            if (StringUtils.isNoneBlank(siteConfigEntity.getSuffix())) {
                boolean b = suffixAvailable(siteConfigEntity.getSuffix());
                if (!b) {
                    throw new MyException(ResultCode.SUFFIX_NOT_AVAILABLE);
                }
            }
        }
        return siteConfigMapper.updateById(siteConfigEntity) > 0;

    }

    @Override
    public boolean deleteOne(Integer id, UserEntity userEntity) {
        SiteConfigEntity siteConfig = findById(id);
        if (siteConfig != null) {
            if (!siteConfig.getUserId().equals(userEntity.getId())) {
                throw new MyException(ResultCode.NOT_HAS_PERMISSION_OPERATOR);
            }
            return siteConfigMapper.deleteById(id) > 0;
        }
        return false;
    }

    @Override
    public boolean suffixAvailable(String suffix) {

        QueryWrapper<SiteConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("suffix", suffix);
        SiteConfigEntity one = siteConfigMapper.selectOne(wrapper);
        return one == null;
    }
}
