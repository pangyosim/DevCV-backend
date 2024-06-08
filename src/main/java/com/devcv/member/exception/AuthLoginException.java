package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class AuthLoginException extends CustomException {

    public AuthLoginException(ErrorCode errorCode) {super(errorCode);}

    public AuthLoginException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
