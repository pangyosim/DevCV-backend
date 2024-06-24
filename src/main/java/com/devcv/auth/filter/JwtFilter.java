package com.devcv.auth.filter;

import com.devcv.auth.jwt.JwtProvider;
import com.devcv.auth.jwt.JwtTokenDto;
import com.devcv.common.exception.ErrorCode;
import com.devcv.common.exception.UnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감, JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        // 1. Request Header 에서 토큰 추출.
        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        // 2. AccessToken 유/무 판별.
        if (StringUtils.hasText(accessToken)) {
            if(jwtProvider.validateToken(accessToken)){
                // SpringContextHolder에 등록.
                this.setAuthentication(accessToken);
            // refreshToken 유/무 판별 -> 유효성 검사
            } else if (!jwtProvider.validateToken(accessToken) && StringUtils.hasText(refreshToken)){
                // refreshToken 유효성검사.
                if(jwtProvider.validateToken(refreshToken)){
                    // 검사완료되면 accessToken 재발급 jwtProvider.refreshTokenDto
                    String email = String.valueOf(jwtProvider.parseClaims(refreshToken).get("email"));
                    JwtTokenDto jwtTokenDto = jwtProvider.refreshTokenDto(email,refreshToken);
                    response.setHeader("Authorization","Bearer "+jwtTokenDto.getAccessToken());
                    response.setHeader("RefreshToken", "Bearer "+refreshToken);
                    this.setAuthentication(jwtTokenDto.getAccessToken());
                } else {
                    throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_ERROR);
                }
            }
        }
        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 : Authentication SecurityContext에 저장. setAuthentication
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
