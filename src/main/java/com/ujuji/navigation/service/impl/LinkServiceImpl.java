package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.LinkMapper;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.BoxService;
import com.ujuji.navigation.service.LinkService;
import com.ujuji.navigation.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userLinkService")
@Slf4j
public class LinkServiceImpl implements LinkService {
    @Resource
    LinkMapper linkMapper;
    @Resource(name = "userBoxService")
    BoxService boxService;

    @Override
    public LinkEntity findById(Integer id) {

        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        try {
            return linkMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(LinkEntity linkEntity, UserEntity userEntity) {
        log.info("insert Link {}", linkEntity.toString());
        BoxEntity box = boxService.findById(linkEntity.getBoxId());
        // 想要增加的链接的盒子不属于他
        if (!box.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }

        int insert = linkMapper.insert(linkEntity);
        return insert > 0;
    }

    @Override
    public boolean update(LinkEntity linkEntity, UserEntity userEntity) {
        log.info("update link {}", linkEntity.toString());
        BoxEntity box = boxService.findById(linkEntity.getBoxId());
        if (box == null) {
            throw new MyException(ResultCode.BOX_NOT_EXISTED);
        }
        // 想要操作的链接的盒子不属于他
        if (!box.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }

        return linkMapper.updateById(linkEntity) > 0;

    }

    @Override
    public boolean deleteById(Integer id, UserEntity userEntity) {
        log.info("delete link {} - {}", id, userEntity.toString());
        LinkEntity linkEntity = findById(id);
        if (linkEntity == null) {
            throw new MyException(ResultCode.LINK_NOT_EXISTED);
        }
        BoxEntity box = boxService.findById(linkEntity.getBoxId());
        if (box == null) {
            throw new MyException(ResultCode.BOX_NOT_EXISTED);
        }
        // 想要操作的链接的盒子不属于他
        if (!box.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }
        return linkMapper.deleteById(linkEntity.getId()) > 0;

    }

/*    @Override
    public boolean deleteById(LinkEntity linkEntity, UserEntity userEntity) {
        BoxEntity box = boxService.findById(linkEntity.getBoxId());
        if (box == null) {
            throw new MyException(ResultCode.BOX_NOT_EXISTED);
        }
        // 想要操作的链接的盒子不属于他
        if (!box.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }
        return linkMapper.deleteById(linkEntity.getId()) > 0;
    }*/

    //暂时先不增加缓存，没到必要的地步
    @Override
    // @Cacheable(key = "#p0", cacheNames = "cache_links")
    public List<LinkEntity> findAllByBoxId(Integer boxId) {
        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("box_id", boxId).orderByDesc("link_order");
        return linkMapper.selectList(wrapper);
    }

    @Override
    public int findCountByBoxId(Integer boxId) {

        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("box_id", boxId);
        return linkMapper.selectCount(wrapper);
    }

    @Override
    public boolean deleteByBoxId(Integer boxId) {

        log.info("deleteByBoxId link boxId:{}", boxId);
        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("box_id", boxId);
        return linkMapper.delete(wrapper) > 0;
    }

    @Override
    public List<LinkEntity> search(String keyword) {
        QueryWrapper<LinkEntity> wrapper = new QueryWrapper<>();
        wrapper.like("title", keyword).or().like("description", keyword).or().like("link", keyword);
        return linkMapper.selectList(wrapper);
    }
}
