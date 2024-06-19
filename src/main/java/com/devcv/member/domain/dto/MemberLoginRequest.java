package com.devcv.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Getter
@AllArgsConstructor
@Builder
public class MemberLoginRequest {
    private String email;
    private String password;
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return UsernamePasswordAuthenticationToken.authenticated(this.email,this.password,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
