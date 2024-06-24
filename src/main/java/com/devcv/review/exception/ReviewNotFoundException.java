package com.devcv.review.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class ReviewNotFoundException extends CustomException {
    public ReviewNotFoundException(ErrorCode errorCode) {super(errorCode);}

    public ReviewNotFoundException(ErrorCode errorCode, String message) {super(errorCode, message);}
}