package com.devcv.common.exception;

public class NotFoundException extends CustomException{

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

