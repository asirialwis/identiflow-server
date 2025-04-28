package com.example.userList.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;


public class JwtUtil {

    private static final String SECRET_KEY = "my-super-strong-secret-key-must-be-32-bytes!";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000; // 24 hours

    public static String generateToken(long userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(EXPIRATION_TIME_MS)))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
