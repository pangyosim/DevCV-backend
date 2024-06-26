package com.devcv.event.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AttendanceListResponse(Long memberId, List<LocalDateTime> attendanceDateList) {

    public static AttendanceListResponse of(Long memberId, List<LocalDateTime> attendanceDateList) {
        return new AttendanceListResponse(memberId, attendanceDateList);
    }
}
