package com.bootjpabase.carmanager.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiReturnCode {

    SERVER_ERROR("서버에서 오류가 발생했습니다.", 500)
    ;

    public static final String RETURN_CODE = "리턴 코드 구분";

    private final String message;
    private final int code;
}
