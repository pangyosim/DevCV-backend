package com.devcv.common.exception;

public class BadRequestException extends CustomException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}