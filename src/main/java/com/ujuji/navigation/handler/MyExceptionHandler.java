package com.ujuji.navigation.handler;

import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.model.entity.BaseEntity;
import com.ujuji.navigation.util.AppResult;
import com.ujuji.navigation.util.AppResultBuilder;
import com.ujuji.navigation.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = MyException.class)
    public AppResult<BaseEntity> entityHandler(MyException e) {
        log.info("Exception Handler: {} - {}", "entityHandler", e.getMessage());
        return AppResultBuilder.fail(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AppResult<Object> argumentsHandler(MethodArgumentNotValidException e) {
        log.info("Exception Handler: {}", "argumentsHandler");
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        log.info("MethodArgumentNotValidException: {}", fieldError.getDefaultMessage());
        return AppResultBuilder.fail(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public AppResult<Object> globalHandler(Exception e) {
        e.printStackTrace();
        log.info("Exception Handler: {}", "globalHandler");
        log.error("系统错误：{}", e.getMessage());
        return AppResultBuilder.fail("系统错误，请联系管理员");
    }

    @ExceptionHandler(value = DisabledException.class)
    public AppResult<Object> disabledExceptionHandler(DisabledException e) {
        e.printStackTrace();
        log.info("disabledExceptionHandler Handler: {}", "DisabledException");
        log.error("DisabledException：{}", e.getMessage());
        return AppResultBuilder.fail("系统错误，请联系管理员");
    }

    /**
     * 这里处理没有权限的异常，不能在SecurityConfig里面处理，因为要被Exception捕获了
     *
     * @param e 异常
     * @return 返回的信息
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public AppResult<Object> accessDeniedHandler(Exception e) {
        log.info("Exception Handler: {}", "accessDeniedHandler");
        log.error("accessDeniedHandler：{}", e.getMessage());
        return AppResultBuilder.fail(ResultCode.PERMISSION_NO_ACCESS);
    }


}
