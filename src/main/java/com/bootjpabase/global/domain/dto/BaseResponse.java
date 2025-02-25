package com.bootjpabase.global.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Base Response")
@Data
@Builder
public class BaseResponse {

    @Schema(description = "TimeStamp")
    private LocalDateTime timeStamp;

    @Schema(description = "HTTP Code")
    private int httpCode;

    @Schema(description = "Message")
    private String message;

    @Schema(description = "Data Size")
    private int dataSize;

    @Schema(description = "Data")
    private Object data;

    public static BaseResponse getBaseResponseBuilder(int httpCode, String message, int dataSize, Object data) {
        return BaseResponse.builder()
                .timeStamp(LocalDateTime.now())
                .httpCode(httpCode)
                .message(message)
                .dataSize(dataSize)
                .data(data)
                .build();
    }

}
