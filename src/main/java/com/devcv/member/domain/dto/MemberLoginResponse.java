package com.devcv.member.domain.dto;

import com.devcv.auth.details.MemberDetails;
import com.devcv.auth.jwt.JwtTokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;

@Getter
@AllArgsConstructor
public class MemberLoginResponse {
    private String memberName;
    private String email;
    private String accessToken;
    private String refreshToken;

    public static MemberLoginResponse from(JwtTokenDto jwtTokenDto, Authentication authentication){
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        return new MemberLoginResponse(
                memberDetails.getMember().getMemberName(), // memberName
                memberDetails.getMember().getEmail(), // email
                jwtTokenDto.getAccessToken(),
                jwtTokenDto.getRefreshToken());
    }
}
