package com.devcv.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefreshToken {
    private String key;
    private String value;
    public RefreshToken updateRefreshToken(String token){
        this.value = token;
        return this;
    }
}
