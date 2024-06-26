package com.devcv.point.presentation;

import com.devcv.auth.filter.SecurityUtil;
import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.ForbiddenException;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.point.application.PointService;
import com.devcv.point.domain.Point;
import com.devcv.point.dto.PointRequestDto;
import com.devcv.point.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final MemberService memberService;

    @GetMapping("/members/{member-id}/points")
    public ResponseEntity<PointResponse> getPointByMemberId(@PathVariable("member-id") Long memberId) {
        if (!memberId.equals(SecurityUtil.getCurrentmemberId())) {
            throw new ForbiddenException(ErrorCode.MEMBER_MISMATCH_EXCEPTION);
        }
        PointResponse pointResponse = pointService.getPointResponse(memberId);
        return ResponseEntity.ok().body(pointResponse);
    }

    //작동 테스트용 임시 메서드
    @PostMapping("/point/save")
    public ResponseEntity<Long> savePoint(@RequestBody PointRequestDto pointRequestDto) {

        Member member = memberService.findMemberBymemberId(pointRequestDto.memberId());

        Point point = pointService.savePoint(member, pointRequestDto.amount(), pointRequestDto.description());

        return ResponseEntity.ok().body(point.getId());
    }

    //작동 테스트용 임시 메서드
    @PostMapping("/point/use")
    public ResponseEntity<Long> usePoint(@RequestBody PointRequestDto pointRequestDto) {

        Member member = memberService.findMemberBymemberId(pointRequestDto.memberId());

        Point point = pointService.usePoint(member, pointRequestDto.amount(), pointRequestDto.description());

        return ResponseEntity.ok().body(point.getId());
    }
}