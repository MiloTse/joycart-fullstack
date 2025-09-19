package com.joycart.backend.config;

import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 * 功能：
 * 1. 拦截每个HTTP请求
 * 2. 检查Authorization头中的JWT token
 * 3. 验证token的有效性
 * 4. 如果token有效，设置用户认证信息到Spring Security上下文
 * 工作流程：
 * 请求 → JWT过滤器 → Spring Security → Controller
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 获取Authorization头
        String authorizationHeader = request.getHeader("Authorization");
        
        String phoneNumber = null;
        String jwtToken = null;

        // 检查Authorization头是否存在且以"Bearer "开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 提取JWT token（去掉"Bearer "前缀）
            jwtToken = authorizationHeader.substring(7);
            
            try {
                // 从token中提取用户手机号
                phoneNumber = jwtUtil.getPhoneNumberFromToken(jwtToken);
                logger.info("JWT token found for phone: {}", phoneNumber);
            } catch (Exception e) {
                logger.warn("Invalid JWT token: {}", e.getMessage());
            }
        } else {
            logger.debug("No Authorization header found or invalid format");
        }

        // 如果提取到了手机号，并且当前没有认证信息
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // 验证token是否有效
                if (jwtUtil.validateToken(jwtToken, phoneNumber)) {
                    
                    // 创建认证对象
                    // 注意：这里我们使用手机号作为principal，密码为null（因为JWT已经验证了身份）
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            phoneNumber,           // principal (用户标识)
                            null,                 // credentials (密码，JWT验证后不需要)
                            new ArrayList<>()     // authorities (权限列表，暂时为空)
                        );
                    
                    // 设置请求详情
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 将认证信息设置到Spring Security上下文中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    
                    logger.info("JWT authentication successful for phone: {}", phoneNumber);
                } else {
                    logger.warn("JWT token validation failed for phone: {}", phoneNumber);
                }
            } catch (Exception e) {
                logger.error("JWT authentication error: {}", e.getMessage());
            }
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
