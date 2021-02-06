package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.CardCodeMapper;
import com.ujuji.navigation.model.dto.SearchDto;
import com.ujuji.navigation.model.entity.CardCodeEntity;
import com.ujuji.navigation.service.CardCodeService;
import com.ujuji.navigation.util.ResultCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardCodeServiceImpl implements CardCodeService {
    @Resource
    private CardCodeMapper cardCodeMapper;

    @Override
    public IPage<CardCodeEntity> findByPage(Integer pageNo, Integer pageSize) {

        IPage<CardCodeEntity> page = new Page<>(pageNo, pageSize);
        QueryWrapper<CardCodeEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return cardCodeMapper.selectPage(page, wrapper);
    }

    @Override
    public CardCodeEntity findById(Integer id) {
        try {
            return cardCodeMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IPage<CardCodeEntity> search(SearchDto searchDto) {
        QueryWrapper<CardCodeEntity> wrapper = new QueryWrapper<>();
        wrapper.like("content", searchDto.getKey());
        IPage<CardCodeEntity> page = new Page<>(searchDto.getPageNo(), searchDto.getPageSize());
        return cardCodeMapper.selectPage(page, wrapper);
    }

    @Override
    public List<String> insertMany(String type, int num) {
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String card = RandomStringUtils.random(Constants.CardCode.CARD_CODE_LENGTH, true, true);
            CardCodeEntity cardCodeEntity = new CardCodeEntity(card, false, type, null);
            int insert = cardCodeMapper.insert(cardCodeEntity);
            if (insert > 0) {
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public String insertOne(String type) {
        String card = RandomStringUtils.random(Constants.CardCode.CARD_CODE_LENGTH, true, true);
        CardCodeEntity cardCodeEntity = new CardCodeEntity(card, false, type, null);
        int insert = cardCodeMapper.insert(cardCodeEntity);
        return insert > 0 ? card : null;
    }

    @Override
    public boolean deleteById(Integer id) {
        return cardCodeMapper.deleteById(id) > 0;
    }

    @Override
    public boolean setUsed(Integer cardCodeId, Integer usedBy) {
        CardCodeEntity byId = findById(cardCodeId);
        if (byId != null) {
            byId.setUsed(true);
            byId.setUsedBy(usedBy);
            int i = cardCodeMapper.updateById(byId);
            return i > 0;
        }
        throw new MyException(ResultCode.SELECT_ONE_FAIL);//记录不存在
    }
}
