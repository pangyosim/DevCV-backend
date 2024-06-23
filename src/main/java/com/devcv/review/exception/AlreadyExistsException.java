package com.devcv.review.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class AlreadyExistsException extends CustomException {

    public AlreadyExistsException(ErrorCode errorCode) {super(errorCode);}

    public AlreadyExistsException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
