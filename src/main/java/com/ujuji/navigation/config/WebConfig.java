package com.ujuji.navigation.config;

import com.ujuji.navigation.interceptor.FrequentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Resource
    CorsDataConfig corsDataConfig;
    @Resource
    FrequentInterceptor frequentInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // String[] strings = new String[corsDataConfig.getOrigins().size()];
        // System.out.println(Arrays.toString(corsDataConfig.getOrigins().toArray(strings)));
        registry.addMapping("/**")
                .allowedOrigins("*")// 这里也就是公开的cors会用到，其余都是在security里面就拦截了
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //    注册Interceptor
        registry.addInterceptor(frequentInterceptor);
    }
}