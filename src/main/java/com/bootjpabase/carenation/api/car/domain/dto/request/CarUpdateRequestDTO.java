package com.bootjpabase.carenation.api.car.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "자동차 수정 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class CarUpdateRequestDTO {

    @NotNull
    @Schema(description = "자동차 순번", example = "1")
    private Long carSn;

    @Schema(description = "카테고리", example = "미니 SUV")
    private String category;

    @Schema(description = "대여 가능 여부(N: 불가능, Y: 가능)", example = "Y")
    private String rentalYn;

    @Schema(description = "대여 상세 내용", example = "수리로 인한 대여 중단")
    private String rentalDescription;
}
