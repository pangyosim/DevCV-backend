package com.devcv.auth.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class JwtUnsupportedException extends CustomException {
    public JwtUnsupportedException(ErrorCode errorCode) { super(errorCode); }

    public JwtUnsupportedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
