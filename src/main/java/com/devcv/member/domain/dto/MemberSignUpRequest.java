package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import com.devcv.member.domain.enumtype.SocialType;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
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

    public Member toMember(PasswordEncoder passwordEncoder) {
        // 소셜회원가입 비밀번호 지정.
        if(this.social != SocialType.normal){
            this.password = "12ff2535dsfsafs21fdsa21sfda11245";
        }
        return Member.builder().memberName(this.memberName).email(this.email).password(passwordEncoder.encode(this.password))
                .nickName(this.nickName).phone(this.phone).address(this.address).social(this.social).company(this.company)
                .job(this.job).stack(this.stack).build();
    }

}
