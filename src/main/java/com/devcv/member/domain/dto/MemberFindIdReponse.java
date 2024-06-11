package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberFindIdReponse {
    private String email;
    public static MemberFindIdReponse from(Member member){
        return new MemberFindIdReponse(member.getEmail());
    }
}
