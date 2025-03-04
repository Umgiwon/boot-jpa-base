package com.bootjpabase.api.car.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "자동차 목록 조회 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class CarListRequestDTO {

    @Schema(description = "카테고리", example = "미니 SUV")
    private String category;

    @Schema(description = "제조사", example = "현대")
    private String manufacturer;

    @Schema(description = "모델명", example = "코나")
    private String modelName;

    @Schema(description = "생산년도", example = "2024")
    private Integer productionYear;

    @Schema(description = "대여 가능 여부(N: 불가능, Y: 가능)", example = "Y")
    private String rentalYn;
}
