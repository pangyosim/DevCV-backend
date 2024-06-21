package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import com.devcv.member.domain.enumtype.CompanyType;
import com.devcv.member.domain.enumtype.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberMypageResponse {
        private Long memberId;
        private String email;
        private String memberName;
        private String phone;
        private CompanyType company;
        private JobType job;
        private List<String> stack;
        private String address;

        public static MemberMypageResponse from(Member member){
                return MemberMypageResponse.builder()
                        .memberId(member.getMemberId())
                        .memberName(member.getMemberName())
                        .phone(member.getPhone())
                        .stack(member.getStack())
                        .job(member.getJob())
                        .company(member.getCompany())
                        .address(member.getAddress())
                        .email(member.getEmail())
                        .build();
        }
}
