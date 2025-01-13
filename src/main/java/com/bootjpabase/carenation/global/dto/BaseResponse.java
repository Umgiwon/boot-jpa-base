package com.bootjpabase.carenation.global.dto;

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

    @Schema(description = "Data")
    private Object data;

    @Schema(description = "Data Size")
    private int dataSize;

    public static BaseResponse getBaseResponseBuilder(int httpCode, String message, Object data, int dataSize) {
        return BaseResponse.builder()
                .timeStamp(LocalDateTime.now())
                .httpCode(httpCode)
                .message(message)
                .data(data)
                .dataSize(dataSize)
                .build();
    }

}
