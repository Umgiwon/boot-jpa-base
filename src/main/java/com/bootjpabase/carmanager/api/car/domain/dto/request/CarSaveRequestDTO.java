package com.bootjpabase.carmanager.api.car.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "자동차 저장 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class CarSaveRequestDTO {

    @NotBlank(message = "카테고리는 필수입니다")
    @Schema(description = "카테고리", example = "미니 SUV")
    private String category;

    @NotBlank(message = "제조사는 필수입니다")
    @Schema(description = "제조사", example = "현대")
    private String manufacturer;

    @NotBlank(message = "모델명은 필수입니다")
    @Schema(description = "모델명", example = "코나")
    private String modelName;

    @NotNull(message = "생산년도는 필수입니다")
    @Schema(description = "생산년도", example = "2024")
    private Integer productionYear;
}