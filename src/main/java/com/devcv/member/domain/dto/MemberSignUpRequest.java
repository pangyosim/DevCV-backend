package com.devcv.member.domain.dto;

import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import com.devcv.member.domain.enumtype.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberSignUpRequest {
    private String memberName;
    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String address;
    private SocialType social;
    private CompanyType company;
    private JobType job;
    private List<String> stack;
}
