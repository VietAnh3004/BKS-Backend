package com.example.BKS_Backend.dto;

import com.example.BKS_Backend.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private Role role;
}
