package com.example.BKS_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    @Builder.Default
    private String type = "Bearer";
    
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private UserAuthDTO userAuth;
}
