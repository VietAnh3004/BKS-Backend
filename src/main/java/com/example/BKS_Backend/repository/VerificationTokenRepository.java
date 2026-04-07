package com.example.BKS_Backend.repository;

import com.example.BKS_Backend.entity.VerificationToken;
import com.example.BKS_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
    int deleteByUser(User user);
}
