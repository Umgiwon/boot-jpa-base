package com.bootjpabase.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.info.title}")
    private String API_TITLE;

    @Value("${springdoc.info.description}")
    private String API_DESCRIPTION;

    @Value("${springdoc.info.version}")
    private String API_VERSION;

    /**
     * Swagger 기본 정보
     *
     * @return
     */
    private Info apiInfo() {
        return new Info()
                .title(API_TITLE) // API의 제목
                .version(API_VERSION) // API의 버전
                .description(API_DESCRIPTION); // API에 대한 설명
    }

    /**
     * Swagger OpenAPI 기본 설정 (JWT 보안 포함)
     */
    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme)
                )
                .info(apiInfo())
                .security(Collections.singletonList(securityRequirement));
    }

    /**
     * 모든 API에 공통 응답 스펙 적용 (400/404/409/500)
     */
    @Bean
    public OpenApiCustomizer globalResponseCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();

                    // 공통 응답 추가 (중복 방지)
                    addIfMissing(responses, "400", "입력값 유효성 검증 실패");
                    addIfMissing(responses, "404", "데이터 오류");
                    addIfMissing(responses, "409", "데이터 중복");
                    addIfMissing(responses, "500", "서버 내부 오류 발생");
                }));
    }

    private void addIfMissing(ApiResponses responses, String code, String description) {
        if (!responses.containsKey(code)) {
            responses.addApiResponse(code, new ApiResponse()
                    .description(description)
                    .content(new Content().addMediaType("application/json",
                            new MediaType().schema(new Schema<>().$ref("#/components/schemas/ExceptionMsg"))))); // Swagger 문서 내부 스키마 참조
        }
    }
}