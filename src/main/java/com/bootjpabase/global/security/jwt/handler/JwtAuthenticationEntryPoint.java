package com.bootjpabase.global.security.jwt.handler;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.exception.ExceptionMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // TokenAuthenticationFilter 에서 던진 BusinessException 예외
        BusinessException businessException = (BusinessException) request.getAttribute("BusinessException");

        ExceptionMsg exceptionMsg;
        if (businessException != null) { // BusinessException 발생 시

            // BusinessException 꺼낸다.
            ApiReturnCode code = businessException.getApiReturnCode();

            exceptionMsg = ExceptionMsg.builder()
                    .success(false)
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .errorCode(code.getCode())
                    .errorMessage(code.getMessage())
                    .build();
        } else {

            exceptionMsg = ExceptionMsg.builder()
                    .success(false)
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .errorCode(ApiReturnCode.UNAUTHORIZED_ERROR.getCode())
                    .errorMessage(ApiReturnCode.UNAUTHORIZED_ERROR.getMessage())
                    .build();
        }

        try (OutputStream ops = response.getOutputStream()) {
            objectMapper.writeValue(ops, new ResponseEntity<>(exceptionMsg, HttpStatus.UNAUTHORIZED).getBody());
            ops.flush();
        }
    }
}
