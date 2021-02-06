package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.LeaveMsgMapper;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;
import com.ujuji.navigation.service.AdminMsgService;
import com.ujuji.navigation.service.LeaveMsgService;
import com.ujuji.navigation.util.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminMsgServiceImpl implements AdminMsgService {
    @Resource
    LeaveMsgService leaveMsgService;
    @Resource
    LeaveMsgMapper leaveMsgMapper;

    @Override
    public IPage<LeaveMsgEntity> findByPage( int pageNo,int pageSize) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        IPage<LeaveMsgEntity> page = new Page<>(pageNo, pageSize);
        return leaveMsgMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean deleteOne(Integer id) {
        final LeaveMsgEntity msgEntity = leaveMsgService.findById(id);
        if (msgEntity == null) {
            throw new MyException(ResultCode.LEAVE_MSG_NOT_EXISTED);
        }
        return leaveMsgMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateOneById(LeaveMsgEntity leaveMsgEntity) {
        return leaveMsgService.updateOne(leaveMsgEntity);
    }
}
