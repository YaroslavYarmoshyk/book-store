package com.online.bookstore.security.service;

public interface JwtService {
    String generateToken(final String email);

    boolean validateToken(final String token);

    String getUsernameFromToken(final String token);
}
