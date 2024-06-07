package com.devcv.common.exception;

public class InternalServerException extends CustomException {

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
