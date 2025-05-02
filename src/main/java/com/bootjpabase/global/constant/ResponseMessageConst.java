package com.bootjpabase.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자
public final class ResponseMessageConst {

    /* 공통 응답 */
    public static final String SUCCESS = "요청에 성공했습니다.";
    public static final String FAIL = "요청에 실패했습니다.";
    public static final String NO_CONTENT = "조회된 데이터가 없습니다.";

    public static final String SAVE_SUCCESS = "저장에 성공했습니다.";
    public static final String SAVE_FAIL = "저장에 실패했습니다.";

    public static final String UPDATE_SUCCESS = "수정에 성공했습니다.";
    public static final String UPDATE_FAIL = "수정에 실패했습니다.";

    public static final String DELETE_SUCCESS = "삭제에 성공했습니다.";
    public static final String DELETE_FAIL = "삭제에 실패했습니다.";

    public static final String SELECT_SUCCESS = "정상적으로 조회되었습니다.";

    /* 공통 로그인 응답 */
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String LOGOUT_SUCCESS = "로그아웃 성공";
    public static final String LOGOUT_FAIL = "로그아웃 실패";
    public static final String LOGIN_ACCESS_TOKEN_SUCCESS = "access token 재발급 성공";
    public static final String LOGIN_ACCESS_TOKEN_FAIL = "access token 재발급 실패";
}
