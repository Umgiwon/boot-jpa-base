package com.bootjpabase.global.filter;

import com.bootjpabase.global.wrapper.CachedBodyHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * POST, PUT, PATCH 요청의 Request Body를 캐싱하기 위한 필터
 */
@Component
public class CachedBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // httpServletRequest 가 아니면 원본 요청을 전달
        if (!(request instanceof HttpServletRequest httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String method = httpRequest.getMethod();
        String contentType = httpRequest.getContentType();

        // Multipart 요청이면 원본 요청을 전달
        if (contentType != null && contentType.startsWith("multipart/")) {
            chain.doFilter(request, response); // multipart 요청이면 캐싱하지 않고 그대로 전달
            return;
        }

        // 요청이 POST, PUT, PATCH일 경우에만 request body 캐싱
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            chain.doFilter(new CachedBodyHttpServletRequest(httpRequest), response);
        } else {
            chain.doFilter(request, response);
        }
    }
}