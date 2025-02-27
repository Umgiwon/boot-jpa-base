package com.bootjpabase.global.enums.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiReturnCode {

    /************** 4XX **************/

    /* token */
    EXPIRED_TOKEN_ERROR("만료된 토큰입니다.", 401),
    UNAUTHORIZED_ERROR("인증이 필요합니다.", 401),
    FORBIDDEN_ERROR("권한이 필요합니다.", 403),

    /* manager */
    LOGIN_ID_FAIL_ERROR("등록된 아이디가 없습니다.", 404),
    LOGIN_PWD_FAIL_ERROR("비밀번호가 틀렸습니다.", 401),
    ID_CONFLICT_ERROR("중복된 아이디 입니다.", 409),
    PHONE_CONFLICT_ERROR("중복된 전화번호 입니다.", 409),
    EMAIL_CONFLICT_ERROR("중복된 이메일 입니다.", 409),

    /************** 5XX **************/
    SERVER_ERROR("서버에서 오류가 발생했습니다.", 500)
    ;

    public static final String RETURN_CODE = "리턴 코드 구분";

    private final String message;
    private final int code;
}
