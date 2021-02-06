package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.LeaveMsgMapper;
import com.ujuji.navigation.model.dto.LeaveMsgDto;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.service.LeaveMsgService;
import com.ujuji.navigation.util.RedisUtils;
import com.ujuji.navigation.util.ResultCode;
import com.ujuji.navigation.util.VerifyCodeCheck;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

@Service
public class LeaveMsgServiceImpl implements LeaveMsgService {
    @Resource
    LeaveMsgMapper leaveMsgMapper;

    @Resource
    VerifyCodeCheck verifyCodeCheck;

    @Resource
    RedisUtils redisUtils;

    @Override
    public IPage<LeaveMsgEntity> searchMsg(String kw, Integer userId, Integer pageSize, Integer pageNo) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        Consumer<QueryWrapper<LeaveMsgEntity>> consumer = leaveMsgEntityQueryWrapper -> leaveMsgEntityQueryWrapper.like("content", kw).or().like("nickname", kw);
        wrapper.eq("user_id", userId).and(consumer).orderByDesc("fixed").orderByDesc("id");
        IPage<LeaveMsgEntity> page = new Page<>(pageNo, pageSize);
        return leaveMsgMapper.selectPage(page, wrapper);
    }

    @Override
    public Integer findNonReadMsg(Integer userId) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("is_read", 0);
        return leaveMsgMapper.selectCount(wrapper);
    }

    @Override
    public IPage<LeaveMsgEntity> findByUserId(Integer userId, Integer pageSize, Integer pageNo) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("fixed").orderByDesc("id");
        IPage<LeaveMsgEntity> page = new Page<>(pageNo, pageSize);
        return leaveMsgMapper.selectPage(page, wrapper);
    }

    @Override
    public LeaveMsgEntity findById(Integer id) {
        try {
            return leaveMsgMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LeaveMsgEntity> findFixedByUserId(Integer userId) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.ne("fixed", 0).eq("user_id", userId).orderByDesc("id");//非置顶
        return leaveMsgMapper.selectList(wrapper);
    }

    @Override
    public IPage<LeaveMsgEntity> findMsgByUserIdWithNoFixed(Integer userId, int pageSize, int pageNo) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("fixed", 0).orderByDesc("id");//非置顶
        IPage<LeaveMsgEntity> page = new Page<>(pageNo, pageSize);
        return leaveMsgMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean setFixed(Integer id, UserEntity userEntity) {
        final LeaveMsgEntity byId = findById(id);
        if (byId == null) {
            throw new MyException(ResultCode.LEAVE_MSG_NOT_EXISTED);//留言不存在
        }
        if (!byId.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);// 无权限
        }
        if (byId.getFixed() == 1) {
            byId.setFixed(0);//取消置顶
        } else {
            byId.setFixed(1);//置顶
        }
        return leaveMsgMapper.updateById(byId) > 0;
    }

    @Override
    public boolean setRead(Integer id, UserEntity userEntity) {
        final LeaveMsgEntity byId = findById(id);
        if (byId == null) {
            throw new MyException(ResultCode.LEAVE_MSG_NOT_EXISTED);//留言不存在
        }
        if (!byId.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);// 无权限
        }
        byId.setIsRead(true);
        return leaveMsgMapper.updateById(byId) > 0;
    }

    @Override
    public int findCountByUserId(Integer userId) {
        QueryWrapper<LeaveMsgEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return leaveMsgMapper.selectCount(wrapper);
    }

    @Override
    public boolean insertOne(LeaveMsgDto leaveMsgDto) {

        // 先验证验证码
        String code = leaveMsgDto.getCode();
        String verifyCode = leaveMsgDto.getVerifyCode();
        verifyCodeCheck.checkVerifyCode(verifyCode, code);

        // 一个人，短时间内只能留言一次
        // 使用通用的限制方法，下面这个就不再限制了
        // long nowMillis = System.currentTimeMillis();
        // Object o = redisUtils.get(Constants.Common.KEY_MSG_INTERVAL + leaveMsgDto.getIp());
        // if (o != null) {
        //     // 还未到可留言时间
        //     throw new MyException(ResultCode.MSG_TIME_TOO_SHORT);
        // }
        //
        // redisUtils.set(Constants.Common.KEY_MSG_INTERVAL + leaveMsgDto.getIp(), nowMillis, 60);//60s

        // 构造留言实体
        LeaveMsgEntity leaveMsgEntity = new LeaveMsgEntity(leaveMsgDto.getUserId(),
                null, leaveMsgDto.getNickname(), leaveMsgDto.getContent(), null, null, leaveMsgDto.getIp());
        leaveMsgEntity.setFixed(0);//不置顶，不能有漏洞

        return leaveMsgMapper.insert(leaveMsgEntity) > 0;
    }

    @Override
    public boolean updateOne(LeaveMsgEntity leaveMsgEntity) {
        if (leaveMsgEntity.getId() == null) {
            throw new MyException(ResultCode.PLEASE_INPUT_ID);
        }
        return leaveMsgMapper.updateById(leaveMsgEntity) > 0;
    }

    @Override
    public boolean deleteOne(Integer id, UserEntity userEntity) {
        final LeaveMsgEntity msgEntity = findById(id);
        if (msgEntity == null) {
            throw new MyException(ResultCode.LEAVE_MSG_NOT_EXISTED);
        }

        if (!msgEntity.getUserId().equals(userEntity.getId())) {
            throw new MyException(ResultCode.PERMISSION_NOT_MATCH_OPERATION);//权限不匹配，留言不属于该用户
        }
        return leaveMsgMapper.deleteById(id) > 0;

    }
}
