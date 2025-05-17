package com.loanapproval.gatewayservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String base64EncodedSecretKey;

    private Key key;

    @PostConstruct
    public void init() {
        // Decode the Base64 string from properties into byte array
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        // Reconstruct the key using the decoded bytes.
        // Keys.hmacShaKeyFor is designed for HMAC-SHA algorithms and will create a SecretKeySpec.
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            return getAllClaimsFromToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // If parsing succeeds, token is validly signed and not expired (parseClaimsJws checks expiration)
        } catch (Exception e) {
            System.err.println("JWT Validation Error in JwtUtil: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) { // Or getSubject
        return getAllClaimsFromToken(token).getSubject();
    }
}
