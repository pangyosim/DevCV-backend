package com.devcv.admin.presentation;

import com.devcv.auth.application.AuthService;
import com.devcv.auth.details.MemberDetails;
import com.devcv.auth.exception.JwtInvalidSignException;
import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.dto.MemberLoginRequest;
import com.devcv.member.domain.dto.MemberLoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/*")
@AllArgsConstructor
public class AdminController {

    private final AuthService authService;

    //----------- login start -----------
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> adminLogin(@RequestBody MemberLoginRequest memberLoginRequest) {
        try {
            MemberLoginResponse resultResponse = authService.login(memberLoginRequest);
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization","Bearer " + resultResponse.getAccessToken());
            return ResponseEntity.ok().headers(header).body(resultResponse);
        } catch (JwtInvalidSignException e) {
            throw new JwtInvalidSignException(ErrorCode.JWT_INVALID_SIGN_ERROR);
        }
    }
    //----------- login end -----------

    @GetMapping("/main")
    public ResponseEntity<?> adminMain() {
        MemberDetails memberDetails = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("/admin/main API RESPONSE");
        return null;
    }

    @GetMapping("/resumes/{resumeId}")
    public ResponseEntity<?> adminGetResume(@PathVariable Long resumeId, @AuthenticationPrincipal MemberDetails memberDetails) {
        System.out.println("/admin/resumes/{resumeId} MemberDetails : " + memberDetails.getMember().getMemberId());
        System.out.println("/admin/resumes/{resumeId} API RESPONSE");
        System.out.println("/admin/resumes/{resumeId} resumeId : " + resumeId);
        return null;
    }

    @PutMapping("/resumes/{resumeId}/status")
    public ResponseEntity<?> adminResumeStatusUpdate(@PathVariable Long resumeId, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("/admin/{resumeId}/status UserDetails : " + userDetails.getUsername());
        System.out.println("/admin/{resumeId}/status API RESPONSE");
        System.out.println("/admin/{resumeId}/status resumeId : " + resumeId);
        return null;
    }
}
