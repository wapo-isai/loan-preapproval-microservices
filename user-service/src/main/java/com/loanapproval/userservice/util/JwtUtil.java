package com.loanapproval.userservice.util;

import com.loanapproval.userservice.persistence.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.security.Key;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String base64EncodedSecretKey;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        // Decode the Base64 string from properties into byte array
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        // Reconstruct the key using the decoded bytes.
        // Keys.hmacShaKeyFor is designed for HMAC-SHA algorithms and will create a SecretKeySpec.
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream().collect(Collectors.toList()));
        claims.put("fullName", user.getFullName());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
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
