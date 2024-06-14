package com.devcv.point.presentation;

import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.point.application.PointService;
import com.devcv.point.domain.Point;
import com.devcv.point.dto.PointRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final MemberService memberService;

    @GetMapping("/point/{member-id}")
    public ResponseEntity<Long> getPointByMemberId(@PathVariable("member-id") Long memberId) {

        Long myPoint = pointService.getMyPoint(memberId);

        return ResponseEntity.ok().body(myPoint);
    }

    //작동 테스트용 임시 메서드
    @PostMapping("/point/save")
    public ResponseEntity<Long> savePoint(@RequestBody PointRequestDto pointRequestDto) {

        Member member = memberService.findMemberByMemberId(pointRequestDto.memberId());

        Point point = pointService.savePoint(member, pointRequestDto.amount(), pointRequestDto.description());

        return ResponseEntity.ok().body(point.getId());
    }

    //작동 테스트용 임시 메서드
    @PostMapping("/point/use")
    public ResponseEntity<Long> usePoint(@RequestBody PointRequestDto pointRequestDto) {

        Member member = memberService.findMemberByMemberId(pointRequestDto.memberId());

        Point point = pointService.usePoint(member, pointRequestDto.amount(), pointRequestDto.description());

        return ResponseEntity.ok().body(point.getId());
    }
}