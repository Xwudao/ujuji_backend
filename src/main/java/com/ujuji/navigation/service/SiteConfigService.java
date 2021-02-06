package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.SiteConfigEntity;
import com.ujuji.navigation.model.entity.UserEntity;

public interface SiteConfigService {

    SiteConfigEntity findById(Integer id);

    SiteConfigEntity findBySuffix(String suffix);

    SiteConfigEntity findByUserId(Integer userId);

    boolean insertOne(SiteConfigEntity siteConfigEntity, UserEntity userEntity);

    boolean updateOne(SiteConfigEntity siteConfigEntity, UserEntity userEntity);

    boolean deleteOne(Integer id, UserEntity userEntity);

    boolean suffixAvailable(String suffix);


}
