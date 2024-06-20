package com.devcv.auth.jwt;


import com.devcv.auth.exception.JwtExpiredException;
import com.devcv.auth.exception.JwtIllegalArgumentException;
import com.devcv.auth.exception.JwtInvalidSignException;
import com.devcv.auth.exception.JwtUnsupportedException;
import com.devcv.common.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private static final String ROLE_TYPE = "role";
    private static final String PK_VALUE = "memberId";
    private static final String MEMBER_NAME = "memberName";
    private static final String MEMBER_EMAIL = "email";
    private static final String SOCIAL_TYPE = "social";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;            // 60분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;


    public JwtProvider (@Value("${keys.jwtkey}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public JwtTokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .claim(PK_VALUE, authorities.split(" ")[3])              // payload "memberId": "name" (ex)
                .claim(ROLE_TYPE, authorities.split(" ")[0])      // payload "role": "일반" (ex)
                .claim(SOCIAL_TYPE, authorities.split(" ")[1])    // payload "social": "일반" (ex)
                .claim(MEMBER_NAME, authorities.split(" ")[2])    // payload "memberName": "홍길동" (ex)
                .claim(MEMBER_EMAIL, authentication.getName())    // payload "email": "testemail@test.com" (ex)
                .setExpiration(accessTokenExpiresIn)                    // payload "exp": 151621022 (ex)
                .signWith(key, SignatureAlgorithm.HS512)                // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtTokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(ROLE_TYPE) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLE_TYPE).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.get("memberId").toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new JwtInvalidSignException(ErrorCode.JWT_INVALID_SIGN_ERROR);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new JwtExpiredException(ErrorCode.JWT_EXPIRED_ERROR);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new JwtUnsupportedException(ErrorCode.JWT_UNSUPPORTED_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new JwtIllegalArgumentException(ErrorCode.JWT_ILLEGALARGUMENT_ERROR);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
