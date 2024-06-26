package com.devcv.resume.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class MultipartException extends CustomException {

    public MultipartException(ErrorCode errorCode) {super(errorCode);}

    public MultipartException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
