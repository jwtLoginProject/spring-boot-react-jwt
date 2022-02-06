package com.tam.jjjwt.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum { //각각의 customized error들 정의
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "E0003"),

    SECURITY_01(HttpStatus.UNAUTHORIZED.value(), "S0001", "권한이 없습니다.", "잘못 입력했음."),

    INVALID_SIGNUP_INPUT_01(HttpStatus.BAD_REQUEST.value(),
            "E1001",
            "Already Existing Id",
            "이미 존재하는 ID입니다."),

    INVALID_SIGNUP_INPUT_02(HttpStatus.BAD_REQUEST.value(),
            "E1002",
            "Invalid Id Format",
            "유효하지 않은 ID입니다. (ID는 3~12자 이내 영대소문자와 숫자 조합"),

    INVALID_SIGNUP_INPUT_03(HttpStatus.BAD_REQUEST.value(),
            "E1003",
            "Invalid Password Format",
            "유효하지 않은 패스워드 형식입니다. (패스워드는 3~12자 이내 영대소문자와 숫자,특수문자 조합"),

    INVALID_SIGNUP_INPUT_04(HttpStatus.BAD_REQUEST.value(),
            "E1004",
            "Invalid Email Format",
            "유효하지 않은 이메일 형식입니다."),

    INVALID_SIGNUP_INPUT_05(HttpStatus.BAD_REQUEST.value(),
            "E1005",
            "Invalid PhoneNum Format",
            "유효하지 않은 전화번호 형식입니다."),

    INVALID_SIGNUP_INPUT_06(HttpStatus.BAD_REQUEST.value(),
            "E1006",
            "Empty Input Value",
            "모든 정보를 입력해주세요.");




    private final int status;
    private final String code;
    private String message;
    private String errorDetail;

    ExceptionEnum(int status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(int status, String code, String message, String errorDetail) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errorDetail = errorDetail;
    }

}

