package com.devcv.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //400

    //401
    UNAUTHORIZED_ERROR("인증에 실패하였습니다."),
    LOGIN_ERROR("아이디 혹은 비밀번호가 틀립니다."),
    LOGIN_ID_ERROR("가입된 아이디가 없습니다."),
    DUPLICATE_ERROR("중복된 아이디입니다."),

    //403

    //404
    NOT_FOUND_ERROR("페이지를 찾을 수 없습니다."),
    NULL_ERROR("데이터 중 하나가 NULL값입니다."),
    //409
    CONFLICT_ERROR("요청 충돌 에러"),

    //500
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    TEST_ERROR("테스트용 에러입니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}