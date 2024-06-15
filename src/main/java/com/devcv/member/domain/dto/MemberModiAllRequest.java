package com.devcv.member.domain.dto;

import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import com.devcv.member.domain.enumtype.RoleType;
import com.devcv.member.domain.enumtype.SocialType;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberModiAllRequest {
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
