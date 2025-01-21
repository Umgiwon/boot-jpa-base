package com.bootjpabase.carmanager.global.filter;

import com.bootjpabase.carmanager.global.enums.ApiReturnCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class TransactionFilter implements Filter {

    ReadOnlyTx readOnlyTx;

    private Map<String, Boolean> readOnlyMap = new HashMap<>();

    public TransactionFilter(ReadOnlyTx readOnlyTx) {
        this.readOnlyTx = readOnlyTx;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        try {
            if (readOnlyRequest(httpReq)) {
                readOnlyTx.doInReadOnly(() -> chain.doFilter(request, response));
            } else {
                chain.doFilter(request, response);
            }

        } catch (ServletException | IOException e) {
            final Map<String, Object> mapBodyException = new HashMap<>();

            mapBodyException.put("success", false);
            mapBodyException.put("path", httpReq.getServletPath());
            mapBodyException.put("timestamp", LocalDateTime.now().toString());
            mapBodyException.put("errorCode", ApiReturnCode.SERVER_ERROR.getCode());
            mapBodyException.put("errorMessage", ApiReturnCode.SERVER_ERROR.getMessage());

            httpRes.setContentType("application/json");
            httpRes.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), mapBodyException);
        }
    }

    private boolean readOnlyRequest(HttpServletRequest httpReq) {
        String method = httpReq.getMethod();
        String uri = httpReq.getRequestURI();
        String key = method + " " + uri;
        return readOnlyMap.containsKey(method + " " + uri)
                || key.equals("GET /test/transaction")

                ;
    }

    @Override
    public void destroy() {
        // empty
    }
}
