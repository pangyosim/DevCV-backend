package com.devcv.common.exception;

public class AuthLoginException extends CustomException{

    public AuthLoginException(ErrorCode errorCode) {super(errorCode);}

    public AuthLoginException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
