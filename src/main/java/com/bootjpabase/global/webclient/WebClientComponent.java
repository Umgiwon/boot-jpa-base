package com.bootjpabase.global.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebClientComponent {

    private final WebClient webClient;

    /**
     * WebClient를 통한 외부 api 호출 로직 (post)
     *
     * @param uri          요청 uri
     * @param token        인증 토큰
     * @param requestBody  요청 본문
     * @param responseType 응답 dto type
     * @return 응답 객체
     *
     * 호출 시 예시) webClientComponent.post("/api/sample", token, SampleSaveRequestDTO, SampleResponseDTO.class);
     */
    public <T, R> R post(String uri, String token, T requestBody, Class<R> responseType) {
        try {
            return webClient.post()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .doOnNext(errorBody ->
                                            log.error("WebClient 오류 응답 ({}): {}", response.statusCode(), errorBody))
                                    .then(Mono.error(new RuntimeException("WebClient API 오류 발생")))
                    )
                    .bodyToMono(responseType) // 200 OK
                    .timeout(Duration.ofSeconds(3))
//                    .doOnNext(body -> log.info("WebClient 응답값: {}", body))
                    .block();

        } catch (WebClientResponseException e) {
            log.error("WebClient 응답 오류: status={}, body={}, message={}",
                    e.getStatusCode().value(), e.getResponseBodyAsString(), e.getMessage());
            throw new RuntimeException("API 호출 실패", e);
        } catch (Exception e) {
            log.error("API 호출 중 예외 발생: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }
}
