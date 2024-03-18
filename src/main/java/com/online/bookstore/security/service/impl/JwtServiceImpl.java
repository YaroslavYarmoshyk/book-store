package com.online.bookstore.security.service.impl;

import com.online.bookstore.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
    private final long expirationTime;
    private final Key secretKey;

    public JwtServiceImpl(@Value("${jwt.expirationTime:86400000}") long expirationTime,
                          @Value("${jwt.secretKey:strongsecretkey2323232egfewgwerggergergrferfe3!}")
                          String secretKey) {
        this.expirationTime = expirationTime;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String generateToken(final String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean validateToken(final String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration).after(new Date());
        } catch (Exception e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }

    @Override
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
