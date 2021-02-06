package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.CardCodeEntity;

import java.util.List;

public interface CardCodeService {

    /**
     * 分页查询
     * @param pageNo 页码
     * @param pageSize 页数
     * @return 结果
     */
    IPage<CardCodeEntity> findByPage(Integer pageNo, Integer pageSize);
    /**
     * 通过ID查询
     *
     * @param id ID
     * @return CardCodeEntity
     */
    CardCodeEntity findById(Integer id);

    /**
     * 批量生成卡密
     *
     * @param type 卡密类型
     * @param num  卡密数量
     * @return 生成的卡密
     */
    List<String> insertMany(String type, int num);

    /**
     * 搜索卡密
     * @param searchDto 卡密
     * @return 搜索到的卡密
     */
    IPage<CardCodeEntity> search(SearchDto searchDto);

    /**
     * 生成单张卡密
     *
     * @param type 卡密类型
     * @return 生成的卡密
     */
    String insertOne(String type);

    /**
     * 删除卡密
     *
     * @param id 卡密ID
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);

    /**
     * 设置该卡密已使用
     *
     * @param cardCodeId 卡密ID
     * @param usedBy     使用的用户ID
     * @return 是否设置成功
     */
    boolean setUsed(Integer cardCodeId, Integer usedBy);


}
