package com.devcv.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //400
    EMPTY_FILE_EXCEPTION("입력받은 이미지 파일이 빈 파일입니다."),
    NO_FILE_EXTENTION("파일 확장자가 없습니다."),
    INVALID_FILE_EXTENTION("유효하지 않은 파일 확장자입니다."),

    //401

    //403

    //404

    //500
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 IO 예외가 발생했습니다."),
    PUT_OBJECT_EXCEPTION("S3에 객체를 저장하는 중 예외가 발생했습니다."),
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    TEST_ERROR("테스트용 에러입니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}