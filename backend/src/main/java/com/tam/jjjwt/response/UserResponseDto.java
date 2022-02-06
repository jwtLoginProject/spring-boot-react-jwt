package com.tam.jjjwt.response;

import lombok.Builder;
import lombok.Data;

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
