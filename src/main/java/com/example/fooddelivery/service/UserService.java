package com.example.fooddelivery.service;

import com.example.fooddelivery.domain.User;
import com.example.fooddelivery.domain.UserRole;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.security.JwtProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 공통 사용자 등록 메서드
    public User registerUser(String email, String rawPassword, String name, String phone, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .name(name)
                .phone(phone)
                .role(role)
                .build();
        return userRepository.save(user);
    }

    // 인증 및 JWT 토큰 생성
    public String authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("로그인 실패");
        }
        return jwtProvider.createToken(user.getEmail(), user.getRole().name());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
