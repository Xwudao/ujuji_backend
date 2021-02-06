package com.ujuji.navigation.service;

import com.ujuji.navigation.model.dto.ForgotPassDto;
import com.ujuji.navigation.model.dto.UpdatePassWithCodeDto;
import com.ujuji.navigation.model.dto.UserDto;
import com.ujuji.navigation.model.entity.BoxEntity;
import com.ujuji.navigation.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserEntity findByUsername(String username);

    UserEntity findById(Integer id);

    UserEntity findByEmail(String email);

    /**
     * 修改密码
     *
     * @param updatePassWithCodeDto 修改的密码的对象
     * @return 是否修改成
     */
    boolean updatePassWithCode(UpdatePassWithCodeDto updatePassWithCodeDto);

    /**
     * 先判断用户和邮箱是否存在，存在的话就发送验证码码
     *
     * @return 是否发送验证码成功
     */
    boolean sendForgotPasswordEmail(ForgotPassDto forgotPassDto);


    Boolean register(UserDto userDto);

    List<BoxEntity> findBoxesAndLinksBySuffix(String suffix);

    /**
     * 备份数据
     *
     * @param userEntity 用户信息
     * @return 备份信息
     */
    List<BoxEntity> findBoxesBackup(UserEntity userEntity);


    /**
     * @param oldPass    旧密码
     * @param newPass    新密码
     * @param userEntity 通过security 得到的用户信息
     * @return 是否修改成功
     */
    boolean updatePassword(String oldPass, String newPass, UserEntity userEntity);

    /**
     * 生成token
     *
     * @return 生成的token
     */
    String generateAccessToken();


    /**
     * 刷新token
     *
     * @param userEntity 用户
     * @param token      token
     * @return 是否添加成功
     */
    boolean updateToken(UserEntity userEntity, String token);

    UserEntity findUserByToken(String token);
}
