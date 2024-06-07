package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class DuplicationException extends CustomException {

    public DuplicationException(ErrorCode errorCode) {super(errorCode);}

    public DuplicationException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
