package com.devcv.auth.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class JwtIllegalArgumentException extends CustomException {
    public JwtIllegalArgumentException(ErrorCode errorCode) { super(errorCode); }

    public JwtIllegalArgumentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
