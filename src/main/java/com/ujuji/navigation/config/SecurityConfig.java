package com.ujuji.navigation.config;

import com.ujuji.navigation.filter.JwtAuthenticationTokenFilter;
import com.ujuji.navigation.filter.JwtLoginFilter;
import com.ujuji.navigation.handler.MyAccessDeniedHandler;
import com.ujuji.navigation.service.UserService;
import com.ujuji.navigation.util.JwtUtils;
import com.ujuji.navigation.util.VerifyCodeCheck;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private VerifyCodeCheck verifyCodeCheck;
    @Resource
    private UserService userService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        //让Spring security放行所有preflight request，也即options预请求要放行
        registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/**", "/").permitAll()
                .antMatchers("/public/**", "/job/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // .addFilterBefore(verifyCodeFilter,
                //         UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtLoginFilter(authenticationManager(), jwtUtils, verifyCodeCheck),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationTokenFilter(userService, jwtUtils),
                        UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
        http.cors();

    }

    @Resource
    CorsDataConfig corsDataConfig;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //指定允许跨域的请求(*所有)：http://wap.ivt.guansichou.com
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        // configuration.setAllowedOrigins(corsDataConfig.getOrigins());
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(corsDataConfig.getHeaders());
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
