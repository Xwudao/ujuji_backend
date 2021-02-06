package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.entity.BoxEntity;

public interface AdminBoxService {
    /**
     * 返回所有盒子
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<BoxEntity> findAllByPage(int pageNum, int pageSize);

    /**
     * 返回所有盒子
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<BoxEntity> searchByPage(String key, int pageNum, int pageSize);


    boolean deleteOne(Integer userId);

    boolean updateOne(BoxEntity userEntity);

}
