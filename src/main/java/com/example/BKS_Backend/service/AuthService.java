package com.example.BKS_Backend.service;

import com.example.BKS_Backend.dto.LoginResponseDTO;
import com.example.BKS_Backend.dto.RegisterResponseDTO;
import com.example.BKS_Backend.dto.UserAuthDTO;
import com.example.BKS_Backend.dto.LoginRequest;
import com.example.BKS_Backend.dto.RegisterRequest;
import com.example.BKS_Backend.dto.UserDTO;
import com.example.BKS_Backend.entity.RefreshToken;
import com.example.BKS_Backend.entity.Role;
import com.example.BKS_Backend.entity.User;
import com.example.BKS_Backend.repository.UserRepository;
import com.example.BKS_Backend.security.CustomUserDetailsService;
import com.example.BKS_Backend.security.JwtUtil;
import com.example.BKS_Backend.dto.RefreshTokenRequest;
import com.example.BKS_Backend.entity.VerificationToken;
import com.example.BKS_Backend.repository.VerificationTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.Duration;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public RegisterResponseDTO register(RegisterRequest request) {
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Định dạng email không hợp lệ!");
        }

        if (!request.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new RuntimeException("Mật khẩu phải chứa ít nhất một chữ cái và một chữ số!");
        }

        if (userRepository.existsByUsername(request.getDisplayName())) {
            throw new RuntimeException("Tên hiển thị đã bị trùng!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = User.builder()
                .username(request.getDisplayName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .displayName(request.getDisplayName())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        VerificationToken verificationToken = VerificationToken.builder()
                .user(savedUser)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(Duration.ofMinutes(15)))
                .build();
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken.getToken());

        return RegisterResponseDTO.builder()
                .user(mapToUserDTO(savedUser))
                .build();
    }

    public LoginResponseDTO login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserAuthDTO userAuthDTO = UserAuthDTO.builder()
                .email(user.getEmail())
                .provider("local")
                .build();

        return LoginResponseDTO.builder()
                .type("Bearer")
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .userAuth(userAuthDTO)
                .user(mapToUserDTO(user))
                .build();
    }

    public Map<String, String> refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(u -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(u.getUsername());
                    String token = jwtUtil.generateToken(userDetails);
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(u.getId());
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("accessToken", token);
                    tokens.put("refreshToken", newRefreshToken.getToken());
                    return tokens;
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is missing or invalid"));
    }

    @Transactional
    public void verifyEmailToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Link xác thực không hợp lệ!"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Link xác thực đã hết hạn!");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDisplayName(
                user.getDisplayName() != null ? user.getDisplayName() : user.getFirstName() + " " + user.getLastName());
        dto.setAvatarKey(user.getAvatarKey());
        dto.setBackgroundKey(user.getBackgroundKey());
        dto.setBio(user.getBio());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setFriendsCount(user.getFriendsCount());
        dto.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        dto.setRole(user.getRole());
        return dto;
    }
}
