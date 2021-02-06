package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.BoxMapper;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.LinkEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.BoxService;
import com.ujuji.navigation.service.LinkService;
import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service("userBoxService")
@Slf4j
public class BoxServiceImpl implements BoxService {
    @Resource
    BoxMapper boxMapper;

    @Resource
    LinkService linkService;
    @Resource
    UserService userService;

    @Override
    public List<BoxEntity> findByUserId(Integer userId) {
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("box_order").orderByAsc("id");
        return boxMapper.selectList(wrapper);
    }

    @Override
    public List<BoxEntity> findByUserIdWithoutPwd(Integer userId) {
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("pwd", "").orderByDesc("box_order").orderByAsc("id");
        return boxMapper.selectList(wrapper);
    }

    @Override
    public BoxEntity findById(Integer id) {
        log.info("find Box");
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        return boxMapper.selectOne(wrapper);
    }

    @Override
    public BoxEntity findByPwd(Integer id, String pwd) {

        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        final BoxEntity boxEntity = boxMapper.selectOne(wrapper);
        if (boxEntity == null) {
            throw new MyException(ResultCode.BOX_NOT_EXISTED);
        }
        if (!StringUtils.equals(boxEntity.getPwd(), pwd)) {
            throw new MyException(ResultCode.BOX_PWD_ERROR);
        }
        //查询出密码
        final List<LinkEntity> allByBoxId = linkService.findAllByBoxId(boxEntity.getId());
        boxEntity.setLinks(allByBoxId);
        return boxEntity;
    }

    @Override
    public BoxEntity findByTitleAndUid(String title, Integer userId) {

        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title).eq("user_id", userId);
        log.info("findByTitleAndUid {} - {}", title, userId);
        try {
            return boxMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(Integer id, UserEntity userInfo) {
        log.info("delete box {} - {}", id, userInfo.toString());
        BoxEntity box = findById(id);
        if (box != null) {
            if (!userInfo.getId().equals(Objects.requireNonNull(box.getUserId()))) {
                throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
            }

            int num = linkService.findCountByBoxId(box.getId());
            if (num > 0) {// 删除其子分类
                linkService.deleteByBoxId(box.getId());
            }

            return boxMapper.deleteById(id) > 0;
        }
        return false;
    }

    @Override
    public boolean insertOne(BoxEntity boxEntity, UserEntity userInfo) {
        log.info("insert box {}", boxEntity.toString());
        BoxEntity box = findByTitleAndUserId(boxEntity);
        if (box != null) {
            throw new MyException(ResultCode.USER_HAS_BOX);
        }
        if (!userInfo.getId().equals(Objects.requireNonNull(boxEntity.getUserId()))) {//只能为
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);
        }
        return boxMapper.insert(boxEntity) > 0;
    }

    @Override
    public BoxEntity findByTitleAndUserId(BoxEntity boxEntity) {
        QueryWrapper<BoxEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", boxEntity.getUserId()).eq("title", boxEntity.getTitle());
        return boxMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateById(BoxEntity boxEntity, UserEntity userInfo) {
        log.info("update box {}", boxEntity.toString());
        if (boxEntity.getId() == null) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }
        BoxEntity box2 = findById(boxEntity.getId());
        if (box2 == null) {
            throw new MyException(ResultCode.BOX_NOT_EXISTED);
        }
        if (!box2.getUserId().equals(userInfo.getId())) {
            throw new MyException(ResultCode.NOT_HAS_PERMISSION_OPERATOR);
        }
        return boxMapper.updateById(boxEntity) > 0;
    }

    public List<BoxEntity> findBoxesByToken(String token) {
        UserEntity userByToken = userService.findUserByToken(token);
        if (userByToken == null) {
            return null;
        }

        return findByUserId(userByToken.getId());
    }


}
