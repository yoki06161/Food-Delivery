package com.example.fooddelivery.config;

import com.example.fooddelivery.security.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    public CustomOAuth2SuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // 예: 구글 OAuth2User 등에서 이메일 or sub claim을 꺼낼 수 있음
        String email = authentication.getName();
        // 실제로는 OAuth2User user = (OAuth2User) authentication.getPrincipal(); 해서 사용자 정보 상세 조회도 가능
        
        // JWT 생성
        String token = jwtProvider.createToken(email, "ROLE_USER");
        
        String redirectUrl = "http://localhost:8080/oauth2/success.html?token=" + token;
        response.sendRedirect(redirectUrl);

    }
}
