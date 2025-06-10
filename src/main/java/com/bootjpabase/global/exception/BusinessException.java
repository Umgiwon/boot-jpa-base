package com.bootjpabase.global.exception;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private ApiReturnCode apiReturnCode;
    private String message;

    public BusinessException(ApiReturnCode apiReturnCode) {
        this.apiReturnCode = apiReturnCode;
    }
}
