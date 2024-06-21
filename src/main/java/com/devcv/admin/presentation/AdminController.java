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
import org.springframework.security.core.context.SecurityContextHolder;
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
        System.out.println(memberDetails.getMember().getMemberRole());
        return null;
    }
}
