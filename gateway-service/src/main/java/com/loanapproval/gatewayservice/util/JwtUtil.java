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
        logger.debug("Base64 Encoded Secret from properties: [{}]", base64EncodedSecretKey); // Log the string
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        // Log the length and maybe a hash or a few bytes to compare, NOT the full keyBytes for security.
        logger.debug("Decoded keyBytes length: {}", keyBytes.length);
        logger.debug("First 5 decoded keyBytes (example): {}, {}, {}, {}, {}", keyBytes.length > 0 ? keyBytes[0] : "N/A", keyBytes.length > 1 ? keyBytes[1] : "N/A", keyBytes.length > 2 ? keyBytes[2] : "N/A", keyBytes.length > 3 ? keyBytes[3] : "N/A", keyBytes.length > 4 ? keyBytes[4] : "N/A" );
        this.key = Keys.hmacShaKeyFor(keyBytes);
        logger.debug("Initialized java.security.Key object: {}", key);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            return getAllClaimsFromToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // If parsing fails (e.g., due to expiration or malformation), treat as expired/invalid
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
