package com.bootjpabase.global.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebClientComponent {

    private final WebClient webClient;

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    /**
     * POST 요청
     */
    public <T, R> R post(String uri, String token, T requestBody, Class<R> responseType) {
        return exchange(HttpMethod.POST, uri, token, requestBody, responseType);
    }

    /**
     * PUT 요청
     */
    public <T, R> R put(String uri, String token, T requestBody, Class<R> responseType) {
        return exchange(HttpMethod.PUT, uri, token, requestBody, responseType);
    }

    /**
     * PATCH 요청
     */
    public <T, R> R patch(String uri, String token, T requestBody, Class<R> responseType) {
        return exchange(HttpMethod.PATCH, uri, token, requestBody, responseType);
    }

    /**
     * GET 요청
     */
    public <R> R get(String uri, String token, Map<String, String> queryParams, Class<R> responseType) {
        String fullUri = buildUriWithQueryParams(uri, queryParams);
        return exchange(HttpMethod.GET, fullUri, token, null, responseType);
    }

    /**
     * DELETE 요청
     */
    public <R> R delete(String uri, String token, Map<String, String> queryParams, Class<R> responseType) {
        String fullUri = buildUriWithQueryParams(uri, queryParams);
        return exchange(HttpMethod.DELETE, fullUri, token, null, responseType);
    }

    /**
     * 공통 WebClient 처리 로직
     *
     * @param method HTTP 메서드
     * @param uri 요청 URI (전체 경로 포함)
     * @param token Authorization 헤더 값
     * @param requestBody 요청 Body (GET/DELETE는 null 가능)
     * @param responseType 응답 DTO 타입
     */
    private <T, R> R exchange(HttpMethod method, String uri, String token, T requestBody, Class<R> responseType) {
        try {

            WebClient.RequestBodySpec request = webClient.method(method)
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, token);

            WebClient.ResponseSpec responseSpec = (requestBody != null)
                    ? request.bodyValue(requestBody).retrieve()
                    : request.retrieve();

            return responseSpec
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .doOnNext(error ->
                                            log.error("WebClient 오류 응답 ({}): {}", response.statusCode(), error))
                                    .then(Mono.error(new RuntimeException("WebClient 오류 발생")))
                    )
                    .bodyToMono(responseType)
                    .timeout(TIMEOUT)
                    .block();

        } catch (Exception e) {
            handleException(e, method, uri);
            throw new RuntimeException("WebClient 호출 실패", e);
        }
    }

    /**
     * WebClient 예외 처리 공통 메서드
     */
    private void handleException(Exception e, HttpMethod method, String uri) {
        if (e instanceof WebClientResponseException we) {
            log.error("WebClient 응답 예외 - ({} {}) -> status: {}, body: {}, message: {}",
                    method.name(), uri, we.getStatusCode(), we.getResponseBodyAsString(), we.getMessage());
        } else {
            log.error("WebClient 요청 예외 - ({} {}) -> {}: {}",
                    method.name(), uri, e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * GET/DELETE 요청을 위한 QueryString 변환
     */
    private String buildUriWithQueryParams(String uri, Map<String, String> queryParams) {
        if (ObjectUtils.isEmpty(queryParams)) return uri;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.toUriString();
    }
}
