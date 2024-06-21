package com.devcv.resume.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class ResumeNotExistException extends CustomException {
    public ResumeNotExistException(ErrorCode errorCode) {super(errorCode);}

    public ResumeNotExistException(ErrorCode errorCode, String message) {super(errorCode, message);}
}