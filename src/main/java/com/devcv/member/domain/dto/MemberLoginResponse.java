package com.devcv.member.domain.dto;

import com.devcv.auth.jwt.JwtTokenDto;
import com.devcv.member.domain.enumtype.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MemberLoginResponse {
    private Long memberId;
    private int social;
    private String memberRole;
    private String accessToken;

    public static MemberLoginResponse from(JwtTokenDto jwtTokenDto, Authentication authentication){
        String[] memberRoleSocial = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")).split(" ");
        return new MemberLoginResponse(Long.valueOf(authentication.getName()),
                SocialType.valueOf(memberRoleSocial[1]).ordinal(), // SocialType
                memberRoleSocial[0] // RoleType
                ,jwtTokenDto.getAccessToken());
    }
}
