package com.devcv.member.domain.dto;

import com.devcv.auth.jwt.JwtTokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MemberLoginResponse {
    private String memberName;
    private String email;
    private String accessToken;

    public static MemberLoginResponse from(JwtTokenDto jwtTokenDto, Authentication authentication){
        String[] memberRoleSocial = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")).split(" ");
        return new MemberLoginResponse(
                memberRoleSocial[2], // memberName
                authentication.getName(), // email
                jwtTokenDto.getAccessToken());
    }
}
