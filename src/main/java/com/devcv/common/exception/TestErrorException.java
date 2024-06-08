package com.devcv.common.exception;

public class TestErrorException extends CustomException {

    public TestErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TestErrorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
