package com.devcv.auth.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class JwtInvalidSignException extends CustomException {
    public JwtInvalidSignException(ErrorCode errorCode) { super(errorCode); }

    public JwtInvalidSignException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
