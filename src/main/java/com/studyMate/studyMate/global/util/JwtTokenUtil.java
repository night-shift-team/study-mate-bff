package com.studyMate.studyMate.global.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final String SECRET = "test_secret_key#@$!test_secret_key#@$!";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Token Expiration Time 설정
     */
    private static final long AC_TOKEN_EXPIRATION = 1000 * 60 * 60 * 1; // 1시간
    private static final long RF_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 24시간

    /**
     * Generate JWT Token (AC | RF)
     */
    public String generateToken(long userId, TokenType tokenType) {
        if(userId == 0L) {
            throw new IllegalStateException("[jwtUtil] invalid user id");
        }

        long currentTime = System.currentTimeMillis();
        long expiredAt = tokenType == tokenType.ACCESS ? currentTime + AC_TOKEN_EXPIRATION : currentTime + RF_TOKEN_EXPIRATION;

        return Jwts.builder()
                .claim("userId", userId)
                .subject(Long.toString(userId))
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expiredAt))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * JWT Token 검증
     * @param token JWT Token
     * @return jwt token claim
     */
    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims;
        } catch (JwtException e) {
            throw new IllegalStateException("[jwtUtil] invalid token",  e);
        }
    }

    private enum TokenType {
        ACCESS, REFRESH
    }
}
