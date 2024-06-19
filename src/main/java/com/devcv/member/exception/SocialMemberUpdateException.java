package com.devcv.member.exception;

import com.devcv.common.exception.CustomException;
import com.devcv.common.exception.ErrorCode;

public class SocialMemberUpdateException extends CustomException {
    public SocialMemberUpdateException(ErrorCode errorCode) {super(errorCode);}

    public SocialMemberUpdateException(ErrorCode errorCode, String message) {super(errorCode, message);}
}
