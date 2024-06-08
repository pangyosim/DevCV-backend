package com.devcv.common.exception.dto;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

    public static ErrorResponse from(CustomException customException) {
        return ErrorResponse.from(customException.getErrorCode());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }
}