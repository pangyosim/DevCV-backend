package com.devcv.member.domain.dto;

import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import com.devcv.member.domain.enumtype.RoleType;
import com.devcv.member.domain.enumtype.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberModifyAllRequest {
    private Long memberId;
    private String memberName;
    private String nickName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private SocialType social;
    private RoleType memberRole;
    private CompanyType company;
    private JobType job;
    private List<String> stack;
}
