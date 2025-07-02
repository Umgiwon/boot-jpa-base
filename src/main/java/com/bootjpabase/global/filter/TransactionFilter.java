package com.bootjpabase.global.filter;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.ExceptionMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * transaction 관리를 처리하는 filter (모든 GET 요청에 대해서 readOnly 처리)
 * <br> JPA 사용시 불필요한 flush를 줄여서 성능 향상(readOnly = true 일 경우)
 * <br> 특정 목적이 있는 경우 filter 차원에서 transaction을 관리하지만,
 * <br> 기본적으로 서비스 계층에서 Transaction 설정하는게 안정적이고 유지보수가 쉽다.
 * <br> filter 차원에서 transaction을 관리할 경우 AOP로 적용되는 @Transactional 과 경계가 중첩될 수 있으니 주의
 */
@Slf4j
//@Component /* 서비스 계층에서 사용하므로 해당 filter 제외 */
@RequiredArgsConstructor
public class TransactionFilter implements Filter {

    private final ReadOnlyTx readOnlyTx;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        String method = httpReq.getMethod();

        try {
            if (HttpMethod.GET.matches(method)) { // GET 요청에 대해서 readOnly
                readOnlyTx.doInReadOnly(() -> chain.doFilter(request, response));
            } else {
                readOnlyTx.doInWrite(() -> chain.doFilter(request, response));
            }
        } catch (Exception e) {
            handleException(httpReq, httpRes, e);
        }
    }

    private void handleException(HttpServletRequest httpReq, HttpServletResponse httpRes, Exception e) throws IOException {
        log.error("요청 처리 중 오류 발생 [{} {}]", httpReq.getMethod(), httpReq.getRequestURI(), e);

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(httpReq.getServletPath())
                .timestamp(LocalDateTime.now())
                .errorCode(ApiReturnCode.SERVER_ERROR.getCode())
                .errorMessage(ApiReturnCode.SERVER_ERROR.getMessage())
                .build();

        httpRes.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpRes.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        new ObjectMapper().writeValue(httpRes.getOutputStream(), exceptionMsg);
    }
}
