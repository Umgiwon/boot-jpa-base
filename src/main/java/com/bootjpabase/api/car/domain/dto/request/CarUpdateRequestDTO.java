package com.bootjpabase.api.car.domain.dto.request;

import com.bootjpabase.api.car.domain.entity.Car;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Car}
 */
@Schema(description = "자동차 수정 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class CarUpdateRequestDTO {

    @Schema(description = "카테고리", example = "미니 SUV")
    private String category;

    @Schema(description = "대여 가능 여부(N: 불가능, Y: 가능)", example = "Y")
    private String rentalYn;

    @Schema(description = "대여 상세 내용", example = "수리로 인한 대여 중단")
    private String rentalDescription;
}
