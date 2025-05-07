package com.bootjpabase.global.config.jwt.controller;

import com.bootjpabase.global.config.jwt.component.TokenProvider;
import com.bootjpabase.global.config.jwt.domain.dto.TokenResponseDTO;
import com.bootjpabase.global.config.jwt.service.TokenService;
import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import com.bootjpabase.global.exception.ExceptionMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token API", description = "토큰 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/token/")
public class TokenController {

    private final TokenService tokenService;
    private final TokenProvider tokenProvider;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "데이터 오류", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
            @ApiResponse(responseCode = "500", description = "서버내부 오류발생", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
    })
    @Operation(summary = "리프레쉬 토큰 검증 후 엑세스 토큰 발급", description = "리프레쉬 토큰 검증 후 엑세스 토큰 발급 API")
    @PostMapping("refresh")
    public BaseResponse<TokenResponseDTO> saveManager(@RequestHeader("Authorization") String token) throws Exception {
        return BaseResponseFactory.successWithMessage(tokenService.refreshAccessToken(token), ResponseMessageConst.LOGIN_ACCESS_TOKEN_SUCCESS);
    }
}
