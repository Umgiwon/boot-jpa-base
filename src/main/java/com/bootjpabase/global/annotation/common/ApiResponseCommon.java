package com.bootjpabase.global.annotation.common;

import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.exception.ExceptionMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "기본 응답", responses = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
})
public @interface ApiResponseCommon {
}