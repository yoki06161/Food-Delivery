package com.example.fooddelivery.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        logger.debug("Processing request URI: {}", path);

        // 퍼블릭 엔드포인트는 필터링을 건너뜁니다.
        if (path.startsWith("/api/v1/auth/") || path.startsWith("/oauth2/") || path.startsWith("/ws/")) {
            logger.debug("Skipping JWT filter for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtProvider.resolveToken(request);
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("JWT token validated and authentication set for user: {}", authentication.getName());
            } else {
                logger.debug("No valid JWT token found.");
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token", e);
            throw new RuntimeException("JWT 필터 처리 중 에러 발생", e);
        }

        filterChain.doFilter(request, response);
    }
}
