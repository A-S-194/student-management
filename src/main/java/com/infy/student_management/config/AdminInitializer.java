package com.infy.student_management.config;

import com.infy.student_management.entity.Role;
import com.infy.student_management.entity.User;
import com.infy.student_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Admin creation during project startup
 */
@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initializeAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(!userRepository.existsByRole(Role.ADMIN)){
                User admin = User.builder()
                        .email("admin@infy.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
