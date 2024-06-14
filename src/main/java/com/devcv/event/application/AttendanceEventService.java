package com.devcv.event.application;

import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.TestErrorException;
import com.devcv.event.domain.AttendanceEvent;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.AttendanceRequest;
import com.devcv.event.repository.AttendanceEventRepository;
import com.devcv.member.application.MemberService;
import com.devcv.member.domain.Member;
import com.devcv.point.application.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        Member member = memberService.findMemberByUserId(request.memberId());

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
}
