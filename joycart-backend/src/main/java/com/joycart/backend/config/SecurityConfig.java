package com.joycart.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 配置类
 * 使用传统的WebSecurityConfigurerAdapter方式，兼容性更好
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 从配置文件读取公开端点
    @Value("${security.public-endpoints}")
    private String publicEndpoints;
    
    // 从配置文件读取允许的CORS源
    @Value("${security.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 将配置字符串转换为数组
        String[] publicEndpointArray = publicEndpoints.split(",");
        // 禁用CSRF保护
        http.csrf().disable()
            // 配置CORS
            .cors().configurationSource(corsConfigurationSource())
            // 配置URL访问权限
                .and()
                .authorizeRequests()
                // 允许这些URL公开访问-无需认证
                .antMatchers(publicEndpointArray).permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()

                .and()
            // 配置session管理：无状态
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * CORS配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 从配置文件读取允许的源地址
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        
        // 允许的HTTP方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // 允许携带认证信息
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
