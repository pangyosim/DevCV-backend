package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class NotSignUpException extends CustomException {

    public NotSignUpException(ErrorCode errorCode) {super(errorCode);}

    public NotSignUpException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
