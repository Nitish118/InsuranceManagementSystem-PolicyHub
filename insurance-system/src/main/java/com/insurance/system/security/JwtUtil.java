package com.insurance.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "insuranceSecretKeyinsuranceSecretKeyinsuranceSecretKey12";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token);
            System.out.println("All claims in token: " + claims);
            
            String role = (String) claims.get("role");
            System.out.println("Extracted raw role from token: " + role);
            
            // Check if role already has ROLE_ prefix, if not add it
            if (role != null) {
                role = role.toUpperCase(); // Always make sure role is uppercase
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }
                System.out.println("Transformed role to: " + role);
            } else {
                // Default to USER role if none specified
                role = "ROLE_USER";
                System.out.println("No role found in token, defaulting to: " + role);
            }
            return role;
        } catch (Exception e) {
            System.err.println("Error extracting role from token: " + e.getMessage());
            e.printStackTrace();
            return "ROLE_USER"; // Default fallback
        }
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims;
        } catch (Exception e) {
            System.err.println("Error parsing JWT claims: " + e.getMessage());
            throw e;
        }
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        // Store role without ROLE_ prefix for simplicity
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        claims.put("role", role.toUpperCase()); // Always store role in uppercase
        System.out.println("Generating token with role: " + role);
        return createToken(claims, username);
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}