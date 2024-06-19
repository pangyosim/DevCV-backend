package com.devcv.review.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class OrderNotFoundException extends CustomException {
    public OrderNotFoundException(ErrorCode errorCode) {super(errorCode);}

    public OrderNotFoundException(ErrorCode errorCode, String message) {super(errorCode, message);}
}