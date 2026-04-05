package com.example.BKS_Backend.service;

import com.example.BKS_Backend.dto.UserDTO;
import com.example.BKS_Backend.entity.User;
import com.example.BKS_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthService authService;

    public UserDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return authService.mapToUserDTO(user);
    }
}
