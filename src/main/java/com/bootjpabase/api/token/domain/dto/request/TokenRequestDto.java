package com.bootjpabase.api.token.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "JWT 토큰 요청 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {

    @Schema(description = "jwt refresh 토큰", example = "Bearer ")
    private String refreshToken;
}
