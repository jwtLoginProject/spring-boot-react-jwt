package com.tam.jjjwt.response.exception;

import com.tam.jjjwt.response.ExceptionEnum;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
