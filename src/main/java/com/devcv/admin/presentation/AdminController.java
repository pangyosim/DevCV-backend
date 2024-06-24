package com.devcv.admin.presentation;

import com.devcv.auth.application.AuthService;
import com.devcv.auth.details.MemberDetails;
import com.devcv.auth.exception.JwtInvalidSignException;
import com.devcv.common.exception.ErrorCode;
import com.devcv.member.domain.dto.MemberLoginRequest;
import com.devcv.member.domain.dto.MemberLoginResponse;
import com.devcv.resume.application.ResumeService;
import com.devcv.resume.domain.enumtype.ResumeStatus;
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
    private final ResumeService resumeService;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_REFRESH_HEADER = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";

    //----------- login start -----------
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> adminLogin(@RequestBody MemberLoginRequest memberLoginRequest) {
        try {
            MemberLoginResponse resultResponse = authService.login(memberLoginRequest);
            HttpHeaders header = new HttpHeaders();
            header.add(AUTHORIZATION_HEADER,BEARER_PREFIX + resultResponse.getAccessToken());
            header.add(AUTHORIZATION_REFRESH_HEADER,BEARER_PREFIX+ resultResponse.getRefreshToken());
            return ResponseEntity.ok().headers(header).body(resultResponse);
        } catch (JwtInvalidSignException e) {
            throw new JwtInvalidSignException(ErrorCode.JWT_INVALID_SIGN_ERROR);
        }
    }
    //----------- login end -----------

    @PutMapping("/resumes/{resumeId}/{status}")
    public ResponseEntity<?> adminUpdateResumeStatus(@PathVariable Long resumeId,@PathVariable ResumeStatus status) {
        resumeService.updateStatus(resumeId, status);
        return ResponseEntity.ok().build();
    }
}
