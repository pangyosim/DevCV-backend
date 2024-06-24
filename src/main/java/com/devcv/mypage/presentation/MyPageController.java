package com.devcv.mypage.presentation;

import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.member.domain.dto.MyPageResponse;
import com.devcv.order.application.OrderService;
import com.devcv.order.domain.dto.OrderListResponse;
import com.devcv.point.application.PointService;
import com.devcv.resume.application.ResumeService;
import com.devcv.resume.domain.dto.ResumeListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mypage/*")
@RequiredArgsConstructor
public class MyPageController {
    private final OrderService orderService;
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final PointService pointService;
    @GetMapping("/{member-id}")
    public ResponseEntity<MyPageResponse> getMyPage(@PathVariable("member-id") Long memberId){
        Member findMember = memberService.findMemberBymemberId(memberId);
        OrderListResponse orderListResponse = orderService.getOrderListByMember(findMember);
        ResumeListResponse resumeListResponse = resumeService.findResumesByMemberId(memberId);
        Long myPoint = pointService.getMyPoint(memberId);
        return ResponseEntity.ok().body(MyPageResponse.from(findMember,
                resumeListResponse
                , orderListResponse,
                myPoint));
    }

}
