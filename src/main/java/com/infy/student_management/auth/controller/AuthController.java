package com.infy.student_management.auth.controller;

import com.infy.student_management.auth.service.AuthService;
import com.infy.student_management.dto.AuthResponse;
import com.infy.student_management.dto.LoginRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequestDto requestDto
            ){
        return ResponseEntity.ok(
                authService.login(requestDto)
        );
    }
}
