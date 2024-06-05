package com.devcv.common.exception;

public class DuplicationException extends CustomException{

    public DuplicationException(ErrorCode errorCode) {super(errorCode);}

    public DuplicationException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
