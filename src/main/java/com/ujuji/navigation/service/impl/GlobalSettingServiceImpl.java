package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.GlobalSettingMapper;
import com.ujuji.navigation.model.entity.GlobalSettingEntity;
import com.ujuji.navigation.service.GlobalSettingService;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class GlobalSettingServiceImpl implements GlobalSettingService {
    @Resource
    GlobalSettingMapper globalSettingMapper;

    @Override
    public GlobalSettingEntity findById(Integer id) {
        try {
            return globalSettingMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GlobalSettingEntity findByName(String name) {
        QueryWrapper<GlobalSettingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);

        try {
            return globalSettingMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<GlobalSettingEntity> findAll() {
        try {
            return globalSettingMapper.selectList(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        // 这里因为在Controller层就验证了权限，只有管理员才能增删改查，所以不用验证用户是谁
        return globalSettingMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean insertOne(GlobalSettingEntity globalSettingEntity) {
        final GlobalSettingEntity byName = findByName(globalSettingEntity.getName());
        if (byName != null) {
            throw new MyException(ResultCode.NAME_EXISTED);
        }
        return globalSettingMapper.insert(globalSettingEntity) > 0;
    }

    @Override
    @Transactional
    public boolean updateById(GlobalSettingEntity globalSettingEntity) {
        if (Objects.isNull(globalSettingEntity.getId())) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }
        return globalSettingMapper.updateById(globalSettingEntity) > 0;
    }
}
