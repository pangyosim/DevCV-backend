package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class SocialDataException extends CustomException {
    public SocialDataException(ErrorCode errorCode) {super(errorCode);}

    public SocialDataException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
