package com.joycart.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // JWT secret key from application.properties
    @Value("${jwt.secret}")
    private String secret;
    
    // JWT expiration time from application.properties
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate JWT token for user
     */
    public String generateToken(Integer userId, String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phoneNumber", phoneNumber);
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract user ID from token
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Integer.class);
    }

    /**
     * Extract phone number from token
     */
    public String getPhoneNumberFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Check if token is expired
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Get expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * Validate token
     */
    public Boolean validateToken(String token, String phoneNumber) {
        final String tokenPhoneNumber = getPhoneNumberFromToken(token);
        return (tokenPhoneNumber.equals(phoneNumber) && !isTokenExpired(token));
    }

    /**
     * Get all claims from token
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}