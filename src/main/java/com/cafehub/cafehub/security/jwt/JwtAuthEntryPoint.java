package com.cafehub.cafehub.security.jwt;

import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object invalidJwt = request.getAttribute("INVALID_JWT");
        Object expiredJwt = request.getAttribute("EXPIRED_JWT");

        if (invalidJwt != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(400);

            ResponseDto<?> message = ResponseDto.fail(ErrorCode.INVALID_TOKEN);

            String result = objectMapper.writeValueAsString(message);
            response.getWriter().write(result);
        } else if (expiredJwt != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(401);

            ResponseDto<?> message = ResponseDto.fail(ErrorCode.ACCESS_TOKEN_EXPIRED);

            String result = objectMapper.writeValueAsString(message);
            response.getWriter().write(result);
        }
    }
}
