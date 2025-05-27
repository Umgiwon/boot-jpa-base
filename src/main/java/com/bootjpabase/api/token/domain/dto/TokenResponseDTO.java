package com.bootjpabase.api.token.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "JWT 토큰 응답 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 객체를 json 으로 직렬화할 때 null 값인 필드를 무시
public class TokenResponseDTO {

    @Schema(description = "jwt access 토큰", example = "Bearer ")
    private String accessToken;

    @Schema(description = "jwt refresh 토큰", example = "Bearer ")
    private String refreshToken;
}
