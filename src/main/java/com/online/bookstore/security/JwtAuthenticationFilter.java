package com.online.bookstore.security;

import com.online.bookstore.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int BEARER_LENGTH = 7;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws IOException {
        try {
            final String token = resolveToken(request);
            if (token != null && jwtService.validateToken(token)) {
                final String username = jwtService.getUsernameFromToken(token);
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                final var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (final Exception exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
        }
    }

    private String resolveToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.substring(BEARER_LENGTH))
                .orElse(null);
    }
}
