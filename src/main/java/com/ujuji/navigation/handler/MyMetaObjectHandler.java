package com.ujuji.navigation.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充.....");
        if (metaObject.hasSetter("createdAt")) {
            log.info("createdAt");
            this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasSetter("updatedAt")) {
            log.info("updatedAt");
            this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充.....");
        if (metaObject.hasSetter("updatedAt")) {
            log.info("updatedAt");
            this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
