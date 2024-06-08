package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long userid;
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getUserId());
    }

}
