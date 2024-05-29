package com.devcv.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //400

    //401

    //403

    //404

    //500
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    TEST_ERROR("테스트용 에러입니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}