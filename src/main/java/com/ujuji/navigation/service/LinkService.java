package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.model.entity.UserEntity;

import java.util.List;

public interface LinkService {

    LinkEntity findById(Integer id);

    boolean insert(LinkEntity linkEntity, UserEntity userEntity);

    boolean update(LinkEntity linkEntity, UserEntity userEntity);

    boolean deleteById(Integer id, UserEntity userEntity);

    List<LinkEntity> findAllByBoxId(Integer boxId);

    /**
     * 根据盒子ID，找到其所有下所有链接
     * @param boxId 盒子id
     * @return 条数
     */
    int findCountByBoxId(Integer boxId);

    /**
     * 删除所有所属于该盒子的链接
     * @param boxId 盒子id
     * @return 是否删除有
     */
    boolean deleteByBoxId(Integer boxId);

    List<LinkEntity> search(String keyword);

}
