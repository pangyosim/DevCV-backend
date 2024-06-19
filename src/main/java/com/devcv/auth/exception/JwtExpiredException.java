package com.devcv.auth.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class JwtExpiredException extends CustomException {
    public JwtExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JwtExpiredException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
