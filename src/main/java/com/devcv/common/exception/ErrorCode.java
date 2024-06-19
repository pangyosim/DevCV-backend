package com.devcv.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //400
    EMPTY_FILE_EXCEPTION("입력받은 이미지 파일이 빈 파일입니다."),
    NO_FILE_EXTENTION("파일 확장자가 없습니다."),
    INVALID_FILE_EXTENTION("유효하지 않은 파일 확장자입니다."),
    EMPTY_VALUE_ERROR("필수 입력 값이 비어있습니다."),

    //401
    UNAUTHORIZED_ERROR("인증에 실패하였습니다."),
    LOGIN_ERROR("아이디 혹은 비밀번호가 틀립니다."),
    FIND_ID_ERROR("가입된 아이디가 없습니다."),
    DUPLICATE_ERROR("중복된 아이디입니다."),

    //403

    //404
    NOT_FOUND_ERROR("페이지를 찾을 수 없습니다."),
    NULL_ERROR("데이터 중 하나가 NULL값입니다."),
    RESUME_NOT_FOUND("이력서를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND("주문 정보를 찾을 수 없습니다"),
    //409
    CONFLICT_ERROR("요청 충돌 에러"),
    REVIEW_ALREADY_EXISTS("이미 이력서에 대한 구매후기를 작성했습니다."),

    //500
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 IO 예외가 발생했습니다."),
    PUT_OBJECT_EXCEPTION("S3에 객체를 저장하는 중 예외가 발생했습니다."),
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    UNAUTHORIZED_TO_COMPLETE_REGISTRATION("이력서 등록 완료 권한이 없습니다."),
    TEST_ERROR("테스트용 에러입니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}