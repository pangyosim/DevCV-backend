package com.devcv.event.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.event.domain.AttendanceEvent;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.AttendanceListResponse;
import com.devcv.event.domain.dto.AttendanceRequest;
import com.devcv.event.repository.AttendanceEventRepository;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.point.application.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceEventService {

    private final static Long ATTENDANCE_POINT = 1000L;

    private final AttendanceEventRepository attendanceEventRepository;
    private final PointService pointService;
    private final MemberService memberService;
    private final EventService eventService;

    @Transactional
    public AttendanceEvent checkAttendance(AttendanceRequest request) {

        Event event = eventService.findByEventId(request.eventId());
        Member member = memberService.findMemberByMemberId(request.memberId());

        checkExist(member, event);
        savePoint(member, event);
        return record(member, event);
    }

    private void checkExist(Member member, Event event) {
        LocalDate today = LocalDate.now();
        if (attendanceEventRepository.existsByMemberAndEventAndDate(member, event, today)) {
            throw new TestErrorException(ErrorCode.TEST_ERROR);
        }
    }

    private AttendanceEvent record(Member member, Event event) {
        AttendanceEvent attendanceEvent = AttendanceEvent.of(member, event);
        return attendanceEventRepository.save(attendanceEvent);
    }

    private void savePoint(Member member, Event event) {
        pointService.savePoint(member, ATTENDANCE_POINT, event.getName());
    }

    @Transactional
    public AttendanceListResponse getAttendanceListResponse(AttendanceRequest request) {
        Member member = memberService.findMemberByMemberId(request.memberId());
        Event event = eventService.findByEventId(request.eventId());

        LocalDate startDate = LocalDate.from(event.getStartDate());
        LocalDate endDate = LocalDate.from(event.getEndDate());

        List<LocalDateTime> attendanceDateList = attendanceEventRepository
                .findCreatedDateByMemberAndDateRange(member, event, startDate, endDate);

        return AttendanceListResponse.of(member.getMemberId(), attendanceDateList);
    }
}