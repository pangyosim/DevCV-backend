package com.devcv.member.domain.dto;

import com.devcv.auth.jwt.JwtTokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static MemberLoginResponse from(JwtTokenDto jwtTokenDto){
        return new MemberLoginResponse(jwtTokenDto.getGrantType(),jwtTokenDto.getAccessToken(),jwtTokenDto.getRefreshToken());
    }
}
