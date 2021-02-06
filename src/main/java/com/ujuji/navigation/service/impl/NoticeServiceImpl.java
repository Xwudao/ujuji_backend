package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.NoticeMapper;
import com.ujuji.navigation.model.entity.NoticeEntity;
import com.ujuji.navigation.service.NoticeService;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    NoticeMapper noticeMapper;

    @Override
    public NoticeEntity findByUserId(Integer userId) {
        QueryWrapper<NoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return noticeMapper.selectOne(wrapper);
    }

    @Override
    public boolean insertOne(NoticeEntity noticeEntity) {
        NoticeEntity userId = findByUserId(noticeEntity.getUserId());
        if (userId != null) {
            throw new MyException(ResultCode.USER_HAS_NOTICE);
        }
        return noticeMapper.insert(noticeEntity) > 0;
    }

    @Override
    public boolean update(NoticeEntity noticeEntity, Integer userId) {
        final NoticeEntity old = findByUserId(userId);
        if (old != null) {
            QueryWrapper<NoticeEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("id", noticeEntity.getId()).eq("user_id", userId);
            return noticeMapper.update(noticeEntity, wrapper) > 0;
        }else{
            return insertOne(noticeEntity);
        }
    }

    @Override
    public boolean deleteOneById(Integer id) {
        return noticeMapper.deleteById(id) > 0;
    }
}
