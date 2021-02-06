package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.UserMapper;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.AdminUserService;
import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;
    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public IPage<UserEntity> findAllByPage(int pageNum, int pageSize) {
        IPage<UserEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return userMapper.selectPage(page, wrapper);
    }


    @Override
    public boolean deleteOne(Integer userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public IPage<UserEntity> searchByPage(String key, int pageNum, int pageSize) {
        IPage<UserEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        try {
            final Integer id = Integer.valueOf(key);
            wrapper.eq("id", id);
        } catch (NumberFormatException e) {
            wrapper.like("username", key).or().like("email", key);
        }
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean updateOne(UserEntity userEntity) {
        if (StringUtils.isBlank(userEntity.getId().toString()) || userEntity.getId() == 0) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }
        UserEntity oldUser = userService.findById(userEntity.getId());
        if (StringUtils.isNoneBlank(userEntity.getEmail()) && !userEntity.getEmail().equals(oldUser.getEmail())) {
            UserEntity byEmail = userService.findByEmail(userEntity.getEmail());
            if (byEmail != null) {
                throw new MyException(ResultCode.EMAIL_EXISTED);
            }
        }

        //修改密码
        if (StringUtils.isNoneBlank(userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        int i = userMapper.updateById(userEntity);
        return i > 0;
    }
}
