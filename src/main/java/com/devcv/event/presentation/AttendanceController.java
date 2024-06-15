package com.devcv.event.presentation;

import com.devcv.event.application.AttendanceEventService;
import com.devcv.event.domain.AttendanceEvent;
import com.devcv.event.domain.dto.AttendanceListResponse;
import com.devcv.event.domain.dto.AttendanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/attendance")
public class AttendanceController {

    private final AttendanceEventService attendanceEventService;

    @PostMapping
    public ResponseEntity<Object> checkAttendance(@RequestBody AttendanceRequest request) {
        AttendanceEvent attendanceEvent = attendanceEventService.checkAttendance(request);
        return ResponseEntity.created(URI.create(String.valueOf(attendanceEvent.getId()))).build();
    }

    @GetMapping
    public ResponseEntity<Object> getAttendanceList(@RequestBody AttendanceRequest request) { //임시 dto
        AttendanceListResponse response = attendanceEventService.getAttendanceListResponse(request);
        return ResponseEntity.ok(response);
    }
}