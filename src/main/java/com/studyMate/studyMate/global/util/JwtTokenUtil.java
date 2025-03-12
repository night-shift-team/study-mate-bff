package com.studyMate.studyMate.global.util;

import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

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
    public String generateToken(String userId, TokenType tokenType) {
        if(Objects.equals(userId, "")) {
            throw new IllegalStateException("[jwtUtil] invalid user id");
        }

        long currentTime = System.currentTimeMillis();
        long expiredAt = tokenType == tokenType.ACCESS ? currentTime + AC_TOKEN_EXPIRATION : currentTime + RF_TOKEN_EXPIRATION;

        return Jwts.builder()
                .claim("userId", userId)
                .subject(userId)
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
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Token Claim 조회
     */
    public String getUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("userId", String.class);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public enum TokenType {
        ACCESS, REFRESH
    }
}
