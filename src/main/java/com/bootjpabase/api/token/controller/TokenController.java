package com.bootjpabase.api.token.controller;

import com.bootjpabase.api.token.domain.dto.request.TokenRequestDto;
import com.bootjpabase.api.token.domain.dto.response.TokenResponseDTO;
import com.bootjpabase.api.token.service.TokenService;
import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token API", description = "토큰 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/token/")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "엑세스 토큰 발급", description = "리프레쉬 토큰으로 검증 후 엑세스 토큰 발급")
    @PostMapping("refresh")
    public BaseResponse<TokenResponseDTO> saveUser(
            @RequestBody TokenRequestDto dto
    ) {
        return BaseResponseFactory.successWithMessage(
                tokenService.refreshAccessToken(dto)
                , ResponseMessageConst.LOGIN_ACCESS_TOKEN_SUCCESS
        );
    }
}
