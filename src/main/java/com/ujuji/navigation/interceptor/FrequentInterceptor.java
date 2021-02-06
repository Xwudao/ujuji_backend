package com.ujuji.navigation.interceptor;

import com.ujuji.navigation.annotation.FrequencyLimit;
import com.ujuji.navigation.core.Constants;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.util.RedisUtils;
import com.ujuji.navigation.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class FrequentInterceptor extends HandlerInterceptorAdapter {
    @Resource
    RedisUtils redisUtils;

    //肯定是preHandle
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //返回true就是直接通过
        if (handler instanceof HandlerMethod) {//是方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String simpleName =
                    handlerMethod.getMethod().getDeclaringClass().getSimpleName() + "."
                            + handlerMethod.getMethod().getName();
            FrequencyLimit methodAnnotation = handlerMethod.getMethodAnnotation(FrequencyLimit.class);
            if (methodAnnotation == null) return true;//直接返回true，放行
            boolean ok = isOk(simpleName, methodAnnotation, request);
            if (!ok) {
                // response
                //抛出错误
                request.setAttribute("filter.error", new MyException(ResultCode.VISIT_TOO_OFTEN));
                request.getRequestDispatcher("/error/throw").forward(request, response);
            }
            return ok;
        }
        // return super.preHandle(request, response, handler);

        return false;
    }

    //判断是否redis中有此记录（有的话，说明采访问了此接口，暂停访问）
    public boolean isOk(String simpleName, FrequencyLimit frequencyLimit, HttpServletRequest request) {
        int value = frequencyLimit.value();
        if (value == 0) {//为0直接false
            return false;
        }
        String ip = request.getRemoteAddr();
        Object o = redisUtils.get(Constants.Common.KEY_FREQUENT_LIMIT + simpleName + "_" + ip);
        if (o == null) {
            //设置限制
            redisUtils.set(Constants.Common.KEY_FREQUENT_LIMIT + simpleName + "_" + ip, "true", value);
            return true;
        }
        return false;
    }
}
