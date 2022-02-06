package com.tam.jjjwt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO implements Serializable {
    private static final long serialVersionUID = 8801985488178486968L;

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    private String code = "SC001";
    private String message;
    private String messageType;
    private Object data;

    public static com.tam.jjjwt.response.BaseResponseDTO success(UserResponseDto.TokenInfo tokenInfo) {
        return com.tam.jjjwt.response.BaseResponseDTO.builder()
                .messageType(SUCCESS)
                .data(tokenInfo)
                .code("SC001")
                .build();
    }

    public static com.tam.jjjwt.response.BaseResponseDTO fail(String message) {
        return com.tam.jjjwt.response.BaseResponseDTO.builder()
                .message(message)
                .messageType(FAIL)
                .code("ER001")
                .build();
    }
}
