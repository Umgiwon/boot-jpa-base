package com.bootjpabase.global.filter;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionFilter implements Filter {

    private final ReadOnlyTx readOnlyTx;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String method = httpReq.getMethod();

        try {
            if ("GET".equalsIgnoreCase(method)) { // GET 요청에 대해서 readOnly
                readOnlyTx.doInReadOnly(() -> chain.doFilter(request, response));
            } else {
                readOnlyTx.doInWrite(() -> chain.doFilter(request, response));
            }
        } catch (ServletException | IOException e) {
            log.error("Error processing request [{} {}]", method, httpReq.getRequestURI(), e);
            handleException(httpReq, httpRes, e);
        }
    }

    private void handleException(HttpServletRequest httpReq, HttpServletResponse httpRes, Exception e) throws IOException {
        Map<String, Object> mapBodyException = Map.of(
                "success", false,
                "path", httpReq.getServletPath(),
                "timestamp", LocalDateTime.now().toString(),
                "errorCode", ApiReturnCode.SERVER_ERROR.getCode(),
                "errorMessage", ApiReturnCode.SERVER_ERROR.getMessage()
        );
        httpRes.setContentType("application/json");
        httpRes.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        new ObjectMapper().writeValue(httpRes.getOutputStream(), mapBodyException);
    }
}
