package com.example.BKS_Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF để tiện test API từ Postman/React sau này
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/health").permitAll() // Cho phép ai cũng vào được link này
                        .anyRequest().authenticated() // Các link khác vẫn bắt đăng nhập
                );

        return http.build();
    }
}