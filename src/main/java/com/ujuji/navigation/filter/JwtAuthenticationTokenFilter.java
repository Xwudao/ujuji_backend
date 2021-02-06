package com.ujuji.navigation.filter;


import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    /*通过构造参数注入*/
    private final UserService userService;

    private final JwtUtils jwtutils;

    public JwtAuthenticationTokenFilter(UserService userService, JwtUtils jwtutils) {
        this.userService = userService;
        this.jwtutils = jwtutils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtutils.getHeader());
        if (StringUtils.isNotEmpty(authHeader)) {

            String[] strings = authHeader.split("\\s");
            if (strings.length >= 2) {
                authHeader = strings[1];
            }

            String username = jwtutils.getUsernameFromToken(authHeader);
            log.info("加入凭证：{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 判断如果username不为空，且上下文中没有数据，那么就尝试验证，
                UserDetails userDetails = this.userService.loadUserByUsername(username);
                log.info("Details:{}", userDetails.toString());


                if (jwtutils.validateToken(authHeader, userDetails)) {
                    // 且验证成功后，在上下文中加入凭证
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}