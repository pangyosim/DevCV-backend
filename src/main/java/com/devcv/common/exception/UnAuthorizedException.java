package com.devcv.common.exception;

public class UnAuthorizedException extends CustomException{

    public UnAuthorizedException(ErrorCode errorCode) {super(errorCode);}

    public UnAuthorizedException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
