package com.tam.jjjwt.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    String userId;
    String accessToken;
    String refreshToken;
    String messageType = "Success";
    String message;


}
