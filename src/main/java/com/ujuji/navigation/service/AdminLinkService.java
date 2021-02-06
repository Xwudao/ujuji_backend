package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.entity.LinkEntity;

public interface AdminLinkService {
    /**
     * 返回所有列表
     *
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<LinkEntity> findAllByPage(int pageNum, int pageSize);

    /**
     * 搜索
     *
     * @param key      关键词
     * @param pageNum  当前页数
     * @param pageSize 每页条数
     * @return IPage
     */
    IPage<LinkEntity> searchByPage(String key, int pageNum, int pageSize);


    boolean deleteOne(Integer userId);

    boolean updateOne(LinkEntity linkEntity);

}
