package com.example.fooddelivery.controller;

import com.example.fooddelivery.dto.SignupRequest;
import com.example.fooddelivery.dto.LoginRequest;
import com.example.fooddelivery.domain.User;
import com.example.fooddelivery.domain.UserRole;
import com.example.fooddelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 공통 사용자 등록 메서드
    private ResponseEntity<?> registerUser(SignupRequest request, UserRole role) {
        User user = userService.registerUser(request.getEmail(), request.getPassword(), request.getName(), request.getPhone(), role);
        return ResponseEntity.ok(user.getId());
    }

    // 고객 등록
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        return registerUser(request, UserRole.CUSTOMER);
    }

    // 라이더 등록
    @PostMapping("/rider/signup")
    public ResponseEntity<?> riderSignup(@Valid @RequestBody SignupRequest request) {
        return registerUser(request, UserRole.RIDER);
    }

    // 오너 등록
    @PostMapping("/owner/signup")
    public ResponseEntity<?> ownerSignup(@Valid @RequestBody SignupRequest request) {
        return registerUser(request, UserRole.OWNER);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
