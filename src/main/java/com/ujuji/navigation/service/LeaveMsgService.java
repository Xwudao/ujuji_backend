package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.LeaveMsgDto;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;
import com.ujuji.navigation.model.entity.UserEntity;

import java.util.List;

public interface LeaveMsgService {
    /**
     * 搜索留言
     *
     * @param kw       关键词
     * @param pageSize 每页大小
     * @param pageNo   第几页
     * @return IPage对象
     */

    IPage<LeaveMsgEntity> searchMsg(String kw, Integer userId, Integer pageSize, Integer pageNo);

    /**
     * 查询没有阅读的留言的个数
     *
     * @param userId 用户ID
     * @return 个数
     */
    Integer findNonReadMsg(Integer userId);

    IPage<LeaveMsgEntity> findByUserId(Integer userId, Integer pageSize, Integer pageNo);

    /**
     * 根据ID查询留言
     *
     * @param id 留言的ID
     * @return 返回留言
     */
    LeaveMsgEntity findById(Integer id);

    /**
     * 查询置顶的留言
     *
     * @param userId 用户id
     * @return 置顶的留言
     */
    List<LeaveMsgEntity> findFixedByUserId(Integer userId);

    /**
     * 返回
     *
     * @param userId 用户ID
     * @return 返回留言
     */
    IPage<LeaveMsgEntity> findMsgByUserIdWithNoFixed(Integer userId, int pageSize, int pageNo);

    /**
     * 设置置顶
     *
     * @param id         待置顶的ID
     * @param userEntity 登录的用户
     * @return 是否置顶成功
     */
    boolean setFixed(Integer id, UserEntity userEntity);

    /**
     * 设置留言已读
     *
     * @param id         留言的ID
     * @param userEntity 用户信息
     * @return 操作是否成功
     */
    boolean setRead(Integer id, UserEntity userEntity);

    /**
     * 查询个数
     *
     * @param userId 用户Id
     * @return 所属该用户的留言条数
     */
    int findCountByUserId(Integer userId);

    /**
     * 留言
     *
     * @param leaveMsgDto 留言
     * @return 是否成功
     */
    boolean insertOne(LeaveMsgDto leaveMsgDto);


    /**
     * 更新留言
     *
     * @param leaveMsgEntity 更新
     * @return 是否更新成功
     */
    boolean updateOne(LeaveMsgEntity leaveMsgEntity);

    /**
     * 删除
     *
     * @param id         删除的留言ID
     * @param userEntity 登录的用户
     * @return 是否删除成功
     */
    boolean deleteOne(Integer id, UserEntity userEntity);
}
