package com.infy.student_management.auth.service;

import com.infy.student_management.dto.AuthResponse;
import com.infy.student_management.dto.LoginRequestDto;
import com.infy.student_management.entity.User;
import com.infy.student_management.repository.UserRepository;
import com.infy.student_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequestDto loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                loginRequest.getEmail()
        );

        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .build();
    }
}
