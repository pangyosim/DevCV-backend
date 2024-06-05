package com.devcv.common.exception;

public class NotNullException extends CustomException{

    public NotNullException(ErrorCode errorCode) {super(errorCode);}

    public NotNullException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
