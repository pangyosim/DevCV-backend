package com.devcv.register.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class S3Exception extends CustomException {

    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }

}
