package com.bootjpabase.carenation.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자
public final class ResponseMessageConst {

    public static final String SELECT_SUCCESS = "조회 성공";
    public static final String NO_CONTENT = "자료 없음";
    public static final String SAVE_SUCCESS = "저장 성공";
    public static final String SAVE_FAIL = "저장 실패";
}
