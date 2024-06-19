package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class SocialLoginException extends CustomException {
    public SocialLoginException(ErrorCode errorCode) {super(errorCode);}
    public SocialLoginException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
