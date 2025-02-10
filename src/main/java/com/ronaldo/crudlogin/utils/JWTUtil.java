package com.ronaldo.crudlogin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    private String secretKey = "483fb8c9262cc2cecb6aa23fd4b2f6365d1e8d19253f43885664aa89ccb541517772d9218df2963356f3da9aa20adda90f35fa104f28cb4a925102dc27d8e3c8f15fb8dbe722b5dac44b0ccca8e9e2b6264f18348473038d9ad3ff8eb0c9e1fbc4b8e03605b8c14365378b134d2c7b0b8ff13ed18e7a9f1c356cbee4fc4880c9";
    private String superRole = "ADMIN";

    public JWTUtil() {
    }

    public String generateToken(String role) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean verifyRole(String jwt, String roleRequest) {
        String role = extractRole(jwt);
        return role != null && (role.equals(roleRequest) || role.equals(superRole));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Token is invalid if any exception occurs during parsing
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

