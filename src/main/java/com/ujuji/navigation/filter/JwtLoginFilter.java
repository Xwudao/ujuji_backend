package com.ujuji.navigation.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujuji.navigation.exception.MyException;
import com.ujuji.navigation.model.dto.UserDto;
import com.ujuji.navigation.model.entity.UserEntity;
import com.ujuji.navigation.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {


    private final JwtUtils jwtUtils;
    private final VerifyCodeCheck verifyCodeCheck;
    private final AuthenticationManager authenticationManager;

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils, VerifyCodeCheck verifyCodeCheck) {

        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
        this.jwtUtils = jwtUtils;
        this.verifyCodeCheck = verifyCodeCheck;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        log.info("Authentication-->>attemptAuthentication");

        // 从输入流中获取到登录的信息
        try {
            final UserDto userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            // 验证 验证码
            final String verifyCode = userDto.getVerifyCode();
            final String code = userDto.getCode();
            log.info("verifyCode ==>>{}", verifyCode);
            log.info("code ==>>{}", code);

            try {
                verifyCodeCheck.checkVerifyCode(verifyCode, code);
            } catch (MyException e) {
                try {
                    request.setAttribute("filter.error", e);
                    request.getRequestDispatcher("/error/throw").forward(request, response);
                } catch (ServletException servletException) {
                    // servletException.printStackTrace();
                }
                // e.printStackTrace();
                return null;
            }

            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserEntity user = (UserEntity) authResult.getPrincipal();
        System.out.println("user:" + user.toString());

        String role = "";
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }

        String token = jwtUtils.generateToken(user);
        //String token = JwtTokenUtils.createToken(user.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        // 获取用户信息
        final SecurityContext context = SecurityContextHolder.getContext();
        Map<String, Object> res = new HashMap<>();
        user.setPassword(null);
        res.put("userInfo", user);
        res.put("token", token);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        AppResult<Object> appResult = AppResultBuilder.success(res, ResultCode.USER_LOGIN_SUCCESS);
        String s = new ObjectMapper().writeValueAsString(appResult);
        PrintWriter writer = response.getWriter();
        writer.print(s);//输出
        writer.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        final String message = failed.getMessage();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        AppResult<String> appResult;

        if (failed instanceof InternalAuthenticationServiceException) {
            appResult = AppResultBuilder.fail(ResultCode.USER_LOGIN_ERROR);
        } else if (failed instanceof DisabledException) {
            appResult = AppResultBuilder.fail(ResultCode.USER_ACCOUNT_FORBIDDEN);
        } else if (StringUtils.isNoneBlank(message)) {
            appResult = AppResultBuilder.fail(message);
        } else {
            appResult = AppResultBuilder.fail(ResultCode.USER_LOGIN_FAIL);
        }
        String s = new ObjectMapper().writeValueAsString(appResult);
        PrintWriter writer = response.getWriter();
        writer.print(s);//输出
        writer.close();
    }
}
