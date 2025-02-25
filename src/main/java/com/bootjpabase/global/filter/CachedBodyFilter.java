package com.bootjpabase.global.filter;

import com.bootjpabase.global.wrapper.CachedBodyHttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * POST, PUT, PATCH 요청의 Request Body를 캐싱하기 위한 필터
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class CachedBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {

            // 요청이 POST, PUT, PATCH일 경우에만 request body 캐싱
            if("POST".equalsIgnoreCase(httpRequest.getMethod())
                    || "PUT".equalsIgnoreCase(httpRequest.getMethod())
                    || "PATCH".equalsIgnoreCase(httpRequest.getMethod())) {

                // request를 래핑
                CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
                chain.doFilter(wrappedRequest, response); // 래핑된 요청 전달
            } else {
                chain.doFilter(request, response); // 요청이 POST, PUT, PATCH가 아니면 원본 요청을 전달
            }
        } else {
            chain.doFilter(request, response); // httpServletRequest가 아니면 원본 요청을 전달
        }
    }
}