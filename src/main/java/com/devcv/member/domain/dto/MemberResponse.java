package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String nickName;
    private String memberName;
    private String email;
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(),member.getNickName(), member.getMemberName(),member.getEmail());
    }
}
