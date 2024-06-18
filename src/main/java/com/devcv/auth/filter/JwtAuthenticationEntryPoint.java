package com.devcv.auth.filter;

import com.devcv.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        sendResponse(response);
    }
    private void sendResponse(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        log.error(ErrorCode.UNAUTHORIZED_ERROR.name() + ": "+ ErrorCode.UNAUTHORIZED_ERROR.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>(){{
            put("errorCode",ErrorCode.UNAUTHORIZED_ERROR.name());
            put("message",ErrorCode.UNAUTHORIZED_ERROR.getMessage());
        }}));
    }
}
