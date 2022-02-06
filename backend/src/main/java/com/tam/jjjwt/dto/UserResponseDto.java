package com.tam.jjjwt.dto;

import lombok.*;

public class UserResponseDto {

    @Builder
    @Data
    public static class TokenInfo {
        public String grantType;
        public String userId;
        public String accessToken;
        public String refreshToken;
        public Long refreshTokenExpirationTime;
    }
}
