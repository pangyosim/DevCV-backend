package com.devcv.auth.filter;

import com.devcv.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = String.valueOf(request.getAttribute("exception"));

        if(exception == null) {
            log.error(ErrorCode.JWT_EXPIRED_ERROR.getMessage());
            sendResponse(response, ErrorCode.JWT_EXPIRED_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.JWT_INVALID_SIGN_ERROR.getMessage())) {
            log.error(ErrorCode.JWT_INVALID_SIGN_ERROR.getMessage());
            sendResponse(response, ErrorCode.JWT_INVALID_SIGN_ERROR);
        }
        //지원되지 않는 토큰일 경우
        else if(exception.equals(ErrorCode.JWT_UNSUPPORTED_ERROR.getMessage())) {
            log.error(ErrorCode.JWT_UNSUPPORTED_ERROR.getMessage());
            sendResponse(response, ErrorCode.JWT_UNSUPPORTED_ERROR);
        }
        else {
            log.error(ErrorCode.JWT_EXPIRED_ERROR.getMessage());
            sendResponse(response, ErrorCode.JWT_EXPIRED_ERROR);
        }
    }
    private void sendResponse(HttpServletResponse response, ErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorCode", exceptionCode.name());
        responseJson.put("message", exceptionCode.getMessage());

        response.getWriter().print(responseJson);
    }
}
