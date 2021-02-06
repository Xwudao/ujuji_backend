package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.NoticeEntity;

public interface NoticeService {

    NoticeEntity findByUserId(Integer userId);

    boolean insertOne(NoticeEntity noticeEntity);

    boolean update(NoticeEntity noticeEntity, Integer userId);

    boolean deleteOneById(Integer id);
}
