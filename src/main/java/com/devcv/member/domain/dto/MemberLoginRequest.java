package com.devcv.member.domain.dto;

import com.devcv.member.domain.enumtype.RoleType;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Getter
public class MemberLoginRequest {
    private String email;
    private String password;

    // 현재 일반로그인만 구현되어있으므로, 일반으로만 설정. 추후 관리자 테이블 생성시 수정 예정.
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return UsernamePasswordAuthenticationToken.authenticated(this.email,this.password,
                Collections.singleton(new SimpleGrantedAuthority(RoleType.일반.name())));
    }
}
