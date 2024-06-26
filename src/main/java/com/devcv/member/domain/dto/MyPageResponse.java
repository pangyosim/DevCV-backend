package com.devcv.member.domain.dto;

import com.devcv.member.domain.Member;
import com.devcv.order.domain.dto.OrderListResponse;
import com.devcv.resume.domain.dto.ResumeListResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponse {
    private MemberMypageResponse memberInfo;
    private ResumeListResponse resumeList;
    private OrderListResponse orderList;
    private Long mypoint;

    public static MyPageResponse from(Member member, ResumeListResponse resumeListResponse, OrderListResponse orderListResponse,Long pointMypageResponse){
        return new MyPageResponse(MemberMypageResponse.from(member),resumeListResponse,orderListResponse,pointMypageResponse);
    }

}
