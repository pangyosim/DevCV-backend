package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class NotNullException extends CustomException {

    public NotNullException(ErrorCode errorCode) {super(errorCode);}

    public NotNullException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
