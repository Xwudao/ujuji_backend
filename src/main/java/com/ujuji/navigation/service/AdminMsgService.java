package com.ujuji.navigation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ujuji.navigation.model.entity.LeaveMsgEntity;

public interface AdminMsgService {

    IPage<LeaveMsgEntity> findByPage(int pageNo, int pageSize);

    boolean deleteOne(Integer id);

    boolean updateOneById(LeaveMsgEntity leaveMsgEntity);

}
