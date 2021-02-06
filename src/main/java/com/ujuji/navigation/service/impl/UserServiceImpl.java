package com.ujuji.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.mapper.UserMapper;
import com.ujuji.navigation.model.dto.ForgotPassDto;
import com.ujuji.navigation.model.dto.UpdatePassWithCodeDto;
import com.ujuji.navigation.model.dto.UserDto;
import com.ujuji.navigation.model.entity.*;
import com.ujuji.navigation.service.*;
import com.ujuji.navigation.util.AuthInfo;
import com.ujuji.navigation.util.ResultCode;
import com.ujuji.navigation.util.VerifyCodeCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    SiteConfigService siteConfigService;
    @Resource
    LinkService linkService;
    @Resource
    BoxService boxService;
    @Resource
    NoticeService noticeService;
    @Resource
    VerifyCodeCheck verifyCodeCheck;
    @Resource
    EmailService emailService;


    @Override
    public boolean updatePassword(String oldPass, String newPass, UserEntity userEntity) {
        if (userEntity == null) {
            throw new MyException(ResultCode.USER_NOT_LOGGED_IN);
        }
        UserEntity user = findById(userEntity.getId());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new MyException(ResultCode.USER_OLD_PASS_ERROR);
        }
        user.setPassword(passwordEncoder.encode(newPass));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public String generateAccessToken() {
        boolean isUnique = false;
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        String genToken = "";
        while (!isUnique) {
            String s = RandomStringUtils.random(30, true, false);
            wrapper.eq("access_token", s);
            UserEntity entity = userMapper.selectOne(wrapper);
            if (entity == null) {
                genToken = s;
                isUnique = true;
            }
        }

        return genToken;
    }

    @Override
    public boolean updateToken(UserEntity userEntity, String token) {
        userEntity.setAccessToken(token);
        return userMapper.updateById(userEntity) > 0;
    }

    @Override
    public UserEntity findUserByToken(String token) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("access_token", token);
        try {
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            log.info("find user by token failed: {}", e.getMessage());
            // e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserEntity findByUsername(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        try {
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        try {
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean updatePassWithCode(UpdatePassWithCodeDto updatePassWithCodeDto) {
        boolean r = verifyCodeCheck.checkPasswordCode(updatePassWithCodeDto.getEmail(),
                updatePassWithCodeDto.getCode());
        log.info("verify result: {}", r);
        if (!r) {
            throw new MyException(ResultCode.VERIFY_CODE_ERROR);//验证码错误
        }
        UserEntity byEmail = findByEmail(updatePassWithCodeDto.getEmail());
        if (byEmail != null) {
            byEmail.setPassword(passwordEncoder.encode(updatePassWithCodeDto.getNewPass()));
            userMapper.updateById(byEmail);
            return true;
        }
        return false;
    }

    @Override
    public boolean sendForgotPasswordEmail(ForgotPassDto forgotPassDto) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", forgotPassDto.getUsername()).eq("email", forgotPassDto.getEmail()).select("id");
        UserEntity userEntity = userMapper.selectOne(wrapper);
        if (userEntity != null) {
            boolean b = emailService.checkHasSent(forgotPassDto);//就发送邮件了
            if (b) {
                emailService.sendForgotPassEmail(forgotPassDto.getEmail());//必须要这里通过代理方式调用，不然不会异步
            }
            return b;
        } else {
            throw new MyException(ResultCode.USERNAME_EMAIL_NOT_MATCH);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity findById(Integer id) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        try {
            return userMapper.selectOne(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean register(UserDto userDto) {
        // 先验证验证码
        String code = userDto.getCode();
        String verifyCode = userDto.getVerifyCode();
        verifyCodeCheck.checkVerifyCode(verifyCode, code);

        UserEntity user = findByUsername(userDto.getUsername());
        if (user != null) {
            throw new MyException(ResultCode.USER_HAS_EXISTED);
        }
        UserEntity user2 = findByEmail(userDto.getEmail());

        if (user2 != null) {
            throw new MyException(ResultCode.EMAIL_EXISTED);
        }
        // 构造userEntity
        UserEntity userEntity = new UserEntity(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), null
                , null, null, null);
        userEntity.setPassword(passwordEncoder.encode(Objects.requireNonNull(userDto.getPassword())));

        final boolean b = userMapper.insert(userEntity) > 0;

        if (userEntity.getId() != null) {
            // 创建配置文件
            SiteConfigEntity config = new SiteConfigEntity("优聚集", "聚集优质网站",
                    null, null, null, null,
                    null, null, null,
                    null, null, null, null, null,
                    null, null, null,
                    userEntity.getId(), 1);
            siteConfigService.insertOne(config, userEntity);
            // 创建公告
            noticeService.insertOne(new NoticeEntity(userEntity.getId(), "", "", 1));
        }
        return b;

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return findByUsername(s);
    }

    @Override
    public List<BoxEntity> findBoxesAndLinksBySuffix(String suffix) {
        final SiteConfigEntity siteConfigEntity = siteConfigService.findBySuffix(suffix);
        if (siteConfigEntity == null) {
            throw new MyException(ResultCode.SUFFIX_NOT_EXISTED);
        }
        final Integer userId = siteConfigEntity.getUserId();
        final List<BoxEntity> boxes = boxService.findByUserId(userId);

        UserEntity userInfo = AuthInfo.getUserInfo();


        List<BoxEntity> boxEntities = new ArrayList<>();
        for (BoxEntity box : boxes) {

            //这里是要判断如果用户已经登录了，且
            if (userInfo != null && userInfo.getId().equals(userId)) {
                box.setPwd(null);
                final List<LinkEntity> links = linkService.findAllByBoxId(box.getId());
                box.setLinks(links);
            } else {
                //否则没登陆
                if (StringUtils.isBlank(box.getPwd())) {// 是没有密码的话，这里才获取列表数据
                    final List<LinkEntity> links = linkService.findAllByBoxId(box.getId());
                    box.setLinks(links);
                } else {
                    box.setPwd("need");//得把真正的密码隐藏了呀
                }
            }
            boxEntities.add(box);
        }
        return boxEntities;

    }

    @Override
    public List<BoxEntity> findBoxesBackup(UserEntity userEntity) {
        final List<BoxEntity> boxes = boxService.findByUserId(userEntity.getId());
        List<BoxEntity> boxEntities = new ArrayList<>();
        for (BoxEntity box : boxes) {
            final List<LinkEntity> links = linkService.findAllByBoxId(box.getId());
            box.setLinks(links);
            boxEntities.add(box);
        }
        return boxEntities;
    }
}
