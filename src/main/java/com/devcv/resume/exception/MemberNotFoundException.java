package com.devcv.resume.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException(ErrorCode errorCode) {super(errorCode);}

    public MemberNotFoundException(ErrorCode errorCode, String message) {super(errorCode, message);}
}