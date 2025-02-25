package com.bootjpabase.api.car.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "자동차 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class CarResponseDTO implements Serializable {

    @Schema(description = "자동차 순번", example = "1")
    private Long carSn;

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

    @Schema(description = "대여 상세 내용", example = "수리로 인한 대여 중단")
    private String rentalDescription;

    @QueryProjection
    public CarResponseDTO(Long carSn, String category, String manufacturer, String modelName, Integer productionYear, String rentalYn, String rentalDescription) {
        this.carSn = carSn;
        this.category = category;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.productionYear = productionYear;
        this.rentalYn = rentalYn;
        this.rentalDescription = rentalDescription;
    }
}
