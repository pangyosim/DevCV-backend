package com.devcv.resume.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class ResumeStatusException extends CustomException {

    public ResumeStatusException(ErrorCode errorCode) {super(errorCode);}

    public ResumeStatusException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
