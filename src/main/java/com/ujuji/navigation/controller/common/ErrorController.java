package com.ujuji.navigation.controller.common;

import com.ujuji.navigation.exception.MyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {
    /**
     * 重新抛出异常
     */
    @RequestMapping("/error/throw")
    public void rethrow(HttpServletRequest request) {
        throw ((MyException) request.getAttribute("filter.error"));
    }
}
