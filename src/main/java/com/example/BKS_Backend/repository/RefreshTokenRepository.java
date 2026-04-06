package com.example.BKS_Backend.repository;

import com.example.BKS_Backend.entity.RefreshToken;
import com.example.BKS_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    int deleteByUser(User user);
}
