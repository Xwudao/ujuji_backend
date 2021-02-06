package com.ujuji.navigation.service;

import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.UserEntity;

import java.util.List;

public interface BoxService {

    /**
     * 获取某个用户的所有分享盒子
     *
     * @param userId 用户id
     * @return 所属属于该用户的盒子
     */
    List<BoxEntity> findByUserId(Integer userId);

    /**
     * 获取某个用户的所有分享盒子，不包含有密码的
     *
     * @param userId 用户id
     * @return 所属属于该用户的盒子不包含有密码的
     */
    List<BoxEntity> findByUserIdWithoutPwd(Integer userId);

    /*根据id查询*/
    BoxEntity findById(Integer id);

    /*通过id和密码查询*/
    BoxEntity findByPwd(Integer id, String pwd);

    /*通过盒子标题和其所属用户ID查询*/
    BoxEntity findByTitleAndUid(String title, Integer userId);

    /**
     * 根据id删除
     *
     * @param id         被删除id
     * @param userEntity 当前登录用户信息实体
     * @return 是否删除成功
     */
    boolean deleteById(Integer id, UserEntity userEntity);

    /**
     * 插入
     *
     * @param boxEntity  实体
     * @param userEntity 当前登录用户信息实体
     * @return 是否插入成功
     */
    boolean insertOne(BoxEntity boxEntity, UserEntity userEntity);

    /**
     * 通过盒子标题和当前登录用户Id查询盒子实体
     *
     * @param boxEntity 包含盒子标题和当前登录用户Id的实体
     * @return 查询到的盒子实体
     */
    BoxEntity findByTitleAndUserId(BoxEntity boxEntity);

    /**
     * 修改
     *
     * @param boxEntity  实体
     * @param userEntity 当前登录用户信息实体
     * @return 是否修改成功
     */
    boolean updateById(BoxEntity boxEntity, UserEntity userEntity);


}
