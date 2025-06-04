package com.example.backendqlks.utils;

import com.example.backendqlks.dao.UserRoleRepository;
import com.example.backendqlks.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private final UserRoleRepository userRoleRepository;

    public JwtUtils(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Value("${jwt.secret}")
    private String SECRET;

    private SecretKey secretKey=null;
    private JwtParser refreshParser = null;
    private JwtParser accessParser = null;

    @PostConstruct
    public void init() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET);
        secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        refreshParser = io.jsonwebtoken.Jwts.parser()
                .require("type", "refresh")
                .verifyWith(secretKey)
                .build();
        accessParser = io.jsonwebtoken.Jwts.parser()
                .require("type", "access")
                .verifyWith(secretKey)
                .build();
    }

    public String generateAccessToken(int accountId, UserRole role) {
        int ttl=2*60*60*1000;
        return Jwts.builder().subject(String.valueOf(accountId))
                .claim("role", role.getName())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(int accountId, UserRole role) {
        int ttl=7*24*60*60*1000;
        return Jwts.builder().subject(String.valueOf(accountId))
                .claim("role", role.getName())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(secretKey)
                .compact();
    }

    public JwtAccountInfo extractAccessToken(String token) throws JwtException {
        var accountId = extractAccessClaim(token, Claims::getSubject);
        var userRole = extractAccessClaim(token, claims -> {
            String role = claims.get("role", String.class);
            return userRoleRepository.findByNameEqualsIgnoreCase(role);
        });
        return userRole.map(role -> new JwtAccountInfo(Integer.parseInt(accountId), role))
                .orElseThrow(() -> new JwtException("Role is invalid in token :v"));
    }

    public String validateAndExtractIdRefreshToken(String token) throws JwtException {
        return extractRefreshClaim(token, Claims::getSubject);
    }

    private <T> T extractAccessClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAccessClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAccessClaims(String token) {
        return accessParser
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractRefreshClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractRefreshClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractRefreshClaims(String token) {
        return refreshParser
                .parseSignedClaims(token)
                .getPayload();
    }
}
