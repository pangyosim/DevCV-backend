package com.devcv.review.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class ReviewAlreadyExistsException extends CustomException {

    public ReviewAlreadyExistsException(ErrorCode errorCode) {super(errorCode);}

    public ReviewAlreadyExistsException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
