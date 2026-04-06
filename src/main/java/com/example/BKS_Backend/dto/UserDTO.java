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
    private String displayName;
    private String avatarKey;
    private String backgroundKey;
    private String bio;
    private boolean emailVerified;
    private int friendsCount;
    private String createdAt;
    private Role role;
}
