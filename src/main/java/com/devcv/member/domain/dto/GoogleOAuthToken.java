package com.devcv.member.domain.dto;

import lombok.Getter;

@Getter
public class GoogleOAuthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private String id_token;
}
