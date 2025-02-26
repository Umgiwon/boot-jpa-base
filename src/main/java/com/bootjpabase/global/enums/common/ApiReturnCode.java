package com.bootjpabase.global.enums.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiReturnCode {

    /* 4XX */
    LOGIN_ID_FAIL_ERROR("등록된 아이디가 없습니다.", 400),
    LOGIN_PWD_FAIL_ERROR("비밀번호가 틀렸습니다.", 400),
    EXPIRED_TOKEN_ERROR("만료된 토큰입니다.", 400),
    UNAUTHORIZED_ERROR("인가되지 않은 사용자입니다.", 400),

    /* 5XX */
    SERVER_ERROR("서버에서 오류가 발생했습니다.", 500)
    ;

    public static final String RETURN_CODE = "리턴 코드 구분";

    private final String message;
    private final int code;
}
