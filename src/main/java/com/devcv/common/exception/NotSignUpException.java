package com.devcv.common.exception;

public class NotSignUpException extends CustomException{

    public NotSignUpException(ErrorCode errorCode) {super(errorCode);}

    public NotSignUpException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
