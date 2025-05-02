package com.bootjpabase.global.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Base Response")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // 객체를 json 으로 직렬화할 때 null 값인 필드를 무시
public class BaseResponse<T> {

    @Schema(description = "TimeStamp")
    private LocalDateTime timeStamp;

    @Schema(description = "HTTP Code")
    private int httpCode;

    @Schema(description = "Message")
    private String message;

    @Schema(description = "Data Size")
    private int dataSize;

    @Schema(description = "Data")
    private T data;

    @Schema(description = "Pagination")
    private Pagination pagination;
}
