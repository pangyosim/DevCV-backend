package com.devcv.member.domain.dto;

import lombok.Getter;

@Getter
public class MemberFindPwPhoneRequest {
    private String memberName;
    private String phone;
}
