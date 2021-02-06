package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.entity.UserEntity;

public interface AdminUserService {
    /**
     * 返回所有用户
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<UserEntity> findAllByPage(int pageNum, int pageSize);

    /**
     * 返搜索回所有用户
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<UserEntity> searchByPage(String key, int pageNum, int pageSize);


    boolean deleteOne(Integer userId);

    boolean updateOne(UserEntity userEntity);


}
