package com.bootjpabase.api.car.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.bootjpabase.api.car.domain.entity.Car}
 */
@Schema(description = "자동차 목록 조회 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
