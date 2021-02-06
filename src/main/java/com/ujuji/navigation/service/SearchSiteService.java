package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.SearchSiteEntity;
import com.ujuji.navigation.model.entity.UserEntity;

import java.util.List;

public interface SearchSiteService {

    // IPage<SearchSiteEntity> findByUserIdAndPage(Integer userId);

    List<SearchSiteEntity> findByUserId(Integer userId);

    SearchSiteEntity findById(Integer id);

    boolean insertOne(SearchSiteEntity noticeEntity);

    boolean update(SearchSiteEntity noticeEntity, UserEntity userEntity);

    boolean deleteOneById(Integer id, UserEntity userEntity);
}
