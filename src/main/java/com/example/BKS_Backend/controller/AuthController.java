package com.example.BKS_Backend.controller;

import com.example.BKS_Backend.dto.ApiResponse;
import com.example.BKS_Backend.dto.LoginRequest;
import com.example.BKS_Backend.dto.RegisterRequest;
import com.example.BKS_Backend.dto.RefreshTokenRequest;
import com.example.BKS_Backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/local")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(authService.register(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PostMapping("/login/local")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(authService.login(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Đăng nhập thất bại: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(authService.refreshToken(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
