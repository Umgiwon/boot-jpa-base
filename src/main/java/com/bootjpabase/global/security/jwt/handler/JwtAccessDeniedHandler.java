package com.bootjpabase.global.security.jwt.handler;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.ExceptionMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errorCode(ApiReturnCode.FORBIDDEN_ERROR.getCode())
                .errorMessage(ApiReturnCode.FORBIDDEN_ERROR.getMessage())
                .build();

        try (OutputStream ops = response.getOutputStream()) {
            objectMapper.writeValue(ops, new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST).getBody());
            ops.flush();
        }
    }
}
