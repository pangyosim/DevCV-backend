package com.devcv.admin.presentation;

import com.devcv.admin.application.AdminService;
import com.devcv.admin.dto.AdminResumeList;
import com.devcv.auth.application.AuthService;
import com.devcv.auth.exception.JwtInvalidSignException;
import com.devcv.common.exception.ErrorCode;
import com.devcv.event.domain.Event;
import com.devcv.event.domain.dto.EventRequest;
import com.devcv.member.domain.dto.MemberLoginRequest;
import com.devcv.member.domain.dto.MemberLoginResponse;
import com.devcv.resume.application.ResumeService;
import com.devcv.resume.domain.dto.ResumeResponse;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/admin/*")
@AllArgsConstructor
public class AdminController {

    private final ResumeService resumeService;
    private final AdminService adminService;

    @PatchMapping("/resumes/{resumeId}/{status}")
    public ResponseEntity<String> adminUpdateResumeStatus(@PathVariable Long resumeId,@PathVariable ResumeStatus status) {
        resumeService.updateStatus(resumeId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resumes")
    public ResponseEntity<AdminResumeList> getAdminResumes(@RequestParam("status") String status) {
        AdminResumeList adminResumeList = adminService.getResumesByStatus(status);
        return ResponseEntity.ok(adminResumeList);
    }

    @GetMapping("/resumes/{resume-id}")
    public ResponseEntity<ResumeResponse> getAdminResumes(@PathVariable("resume-id") Long resumeId) {
        ResumeResponse resumeResponse = adminService.getResume(resumeId);
        return ResponseEntity.ok(resumeResponse);
    }

    @PostMapping("/events")
    public ResponseEntity<Object> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = adminService.createEvent(eventRequest);
        return ResponseEntity.created(URI.create(String.valueOf(event.getId()))).build();
    }
}