package com.devcv.resume.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class HttpMessageNotReadableException extends CustomException {

    public HttpMessageNotReadableException(ErrorCode errorCode) {super(errorCode);}

    public HttpMessageNotReadableException(ErrorCode errorCode, String message) {super(errorCode, message);}

}
