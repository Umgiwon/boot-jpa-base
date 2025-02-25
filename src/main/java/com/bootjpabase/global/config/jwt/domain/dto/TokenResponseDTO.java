package com.bootjpabase.global.config.jwt.domain.dto;

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
public class TokenResponseDTO {

    @Schema(description = "jwt 토큰", example = "Bearer ")
    private String token;
}
