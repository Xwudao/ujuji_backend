package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.GlobalSettingEntity;

import java.util.List;

public interface GlobalSettingService {
    GlobalSettingEntity findById(Integer id);

    GlobalSettingEntity findByName(String name);

    List<GlobalSettingEntity> findAll();

    boolean deleteById(Integer id);

    boolean insertOne(GlobalSettingEntity globalSettingEntity);

    boolean updateById(GlobalSettingEntity globalSettingEntity);


}
