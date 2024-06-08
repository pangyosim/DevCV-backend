package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long userId;
    private String nickName;
    private String userName;
    private String email;
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getUserId(),member.getNickName(), member.getUserName(),member.getUserName());
    }
}
