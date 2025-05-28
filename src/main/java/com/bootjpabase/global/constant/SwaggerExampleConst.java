package com.bootjpabase.global.constant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 스웨거 예제 json 상수
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지를 위한 private 생성자
public class SwaggerExampleConst {

    /* ==================== Sample ==================== */
    public static final String SAMPLE_SAVE_EXAMPLE_1 = """
                [
                    {
                        "title": "title11",
                        "content": "content11"
                    }
                ]
            """;

    public static final String SAMPLE_SAVE_LIST_EXAMPLE_1 = """
                [
                    {
                        "title": "title11",
                        "content": "content11"
                    },
                    {
                        "title": "title12",
                        "content": "content12"
                    }
                ]
            """;

    public static final String SAMPLE_UPDATE_EXAMPLE_1 = """
                {
                    "title": "수정된 제목1",
                    "content": "수정된 내용1"
                }
            """;

    /* ==================== Car ==================== */
    public static final String CAR_SAVE_EXAMPLE_1 = """
            
                [
                    {
                        "category": "준중형 SUV",
                        "manufacturer": "현대",
                        "modelName": "아이오닉",
                        "productionYear": "2024"
                    }
                ]
            """;

    public static final String CAR_SAVE_LIST_EXAMPLE_1 = """
            
                [
                    {
                        "category": "준중형 SUV",
                        "manufacturer": "현대",
                        "modelName": "아이오닉",
                        "productionYear": "2024"
                    },
                    {
                        "category": "대형 RV",
                        "manufacturer": "현대",
                        "modelName": "스타리아",
                        "productionYear": "2022"
                    }
                ]
            """;

    public static final String CAR_UPDATE_EXAMPLE_1 = """
                {
                    "category": "미니 SUV",
                    "rentalYn": "Y",
                    "rentalDescription": "수리로 인한 대여 중단"
                }
            """;

}
