package com.joycart.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

 import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
 import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
 import com.google.api.client.http.javanet.NetHttpTransport;
 import com.google.api.client.json.gson.GsonFactory;
 import java.util.Collections;

/**
 * Google ID Token验证工具类
 * 用于验证Google登录返回的ID Token
 */
@Component
public class GoogleTokenVerifier {

    private static final Logger logger = LoggerFactory.getLogger(GoogleTokenVerifier.class);

    // Google OAuth2 客户端ID（从配置文件读取）
    @Value("${google.oauth2.client-id:}")
    private String googleClientId;

     private GoogleIdTokenVerifier verifier;

    /**
     * 初始化GoogleIdTokenVerifier
     * 注意：由于暂时没有完整的调用链，这里使用日志输出以便调试
     * 
     * 当前实现：使用日志输出模拟初始化过程
     * 后续实现：使用GoogleIdTokenVerifier进行实际验证
     */
    private void initializeVerifier() {
        logger.info("=== Initializing GoogleIdTokenVerifier ===");
        logger.debug("Google Client ID configured: {}", googleClientId != null && !googleClientId.isEmpty());

        if (googleClientId == null || googleClientId.trim().isEmpty()) {
            logger.warn("⚠️ Google Client ID is not configured");
            logger.warn("Please set google.oauth2.client-id in application.properties");
            logger.warn("Token verification will fail until Client ID is configured");
        } else {
            logger.info("✅ Google Client ID is configured");
            logger.debug("Client ID (first 10 chars): {}", 
                    googleClientId.length() > 10 ? googleClientId.substring(0, 10) + "..." : googleClientId);
            
              verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                     .setAudience(Collections.singletonList(googleClientId))
                     .build();
             logger.info("GoogleIdTokenVerifier initialized successfully");
        }
        logger.info("=== GoogleIdTokenVerifier Initialization Completed ===");
    }

    /**
     * 验证Google ID Token并提取用户信息
     * 
     * @param idToken Google返回的ID Token
     * @return 包含用户信息的Map，如果验证失败返回null
     */
    public Map<String, Object> verifyIdToken(String idToken) {
        logger.info("=== Google ID Token Verification Started ===");
        logger.debug("ID Token length: {}", idToken != null ? idToken.length() : 0);
        logger.debug("ID Token (first 50 chars): {}", idToken != null && idToken.length() > 50 
                ? idToken.substring(0, 50) + "..." : idToken);

        if (idToken == null || idToken.trim().isEmpty()) {
            logger.warn("ID Token is null or empty - verification failed");
            return null;
        }

        try {
            // 初始化Verifier
            initializeVerifier();

             if (verifier == null) {
                logger.error("GoogleIdTokenVerifier is not initialized - Client ID not configured");
                return null;
            }

            logger.debug("Verifying Google ID Token...");

            // 验证Token
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                logger.warn("Google ID Token verification failed - Token is invalid");
                return null;
            }

            // 获取Payload
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            logger.info("✅ Google ID Token verified successfully");
            logger.debug("Token payload - Email: {}", payload.getEmail());
            logger.debug("Token payload - Name: {}", payload.get("name"));
            logger.debug("Token payload - Picture: {}", payload.get("picture"));
            logger.debug("Token payload - Subject (Google ID): {}", payload.getSubject());

            // 将Payload转换为Map
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", payload.getEmail());
            userInfo.put("name", payload.get("name"));
            userInfo.put("picture", payload.get("picture"));
            userInfo.put("sub", payload.getSubject()); // Google用户ID
            userInfo.put("emailVerified", payload.getEmailVerified());

            logger.info("=== Google ID Token Verification Completed Successfully ===");
            return userInfo;

        } catch (Exception e) {
            logger.error("❌ Google ID Token verification failed: {}", e.getMessage(), e);
            logger.error("Error class: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
            return null;
        }
    }

    /**
     * 从验证后的Payload中提取email
     * 
     * @param payload Token验证后的Payload
     * @return 用户email，如果不存在返回null
     */
    public String extractEmail(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("Payload is null, cannot extract email");
            return null;
        }

        String email = (String) payload.get("email");
        logger.debug("Extracted email from payload: {}", email);
        return email;
    }

    /**
     * 从验证后的Payload中提取Google用户ID
     * 
     * @param payload Token验证后的Payload
     * @return Google用户ID（sub字段），如果不存在返回null
     */
    public String extractGoogleId(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("Payload is null, cannot extract Google ID");
            return null;
        }

        String googleId = (String) payload.get("sub");
        logger.debug("Extracted Google ID from payload: {}", googleId);
        return googleId;
    }

    /**
     * 从验证后的Payload中提取用户名称
     * 
     * @param payload Token验证后的Payload
     * @return 用户名称，如果不存在返回null
     */
    public String extractName(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("Payload is null, cannot extract name");
            return null;
        }

        String name = (String) payload.get("name");
        logger.debug("Extracted name from payload: {}", name);
        return name;
    }

    /**
     * 从验证后的Payload中提取用户头像URL
     * 
     * @param payload Token验证后的Payload
     * @return 用户头像URL，如果不存在返回null
     */
    public String extractPicture(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("Payload is null, cannot extract picture");
            return null;
        }

        String picture = (String) payload.get("picture");
        logger.debug("Extracted picture from payload: {}", picture);
        return picture;
    }
}

