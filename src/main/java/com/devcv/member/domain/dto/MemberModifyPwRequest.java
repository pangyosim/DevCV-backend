package com.devcv.member.domain.dto;

import lombok.Getter;

@Getter
public class MemberModifyPwRequest {
    private String password;
    private Long memberId;
}
