package com.example.BKS_Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String displayName;

    private String bio;

    private String backgroundKey;

    private String avatarKey;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean emailVerified = false;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "int default 0")
    private int friendsCount = 0;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
