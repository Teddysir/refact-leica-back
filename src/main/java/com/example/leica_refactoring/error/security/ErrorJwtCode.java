package com.example.leica_refactoring.error.security;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorJwtCode {

    INVALID_JWT_FORMAT("1001","1001 INVALID JWT FORMAT"),
    EXPIRED_JWT_TOKEN("1002","1002 EXPIRED JWT TOKEN"),
    UNSUPPORTED_JWT_TOKEN("1003","1003 UNSUPPORTED JWT TOKEN"),
    INVALID_VALUE("1004","1004 INVALID VALUE"),
    RUNTIME_EXCEPTION("1005","1005 RUNTIME EXCEPTION");



    private final String code;
    private final String message;

    ErrorJwtCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}