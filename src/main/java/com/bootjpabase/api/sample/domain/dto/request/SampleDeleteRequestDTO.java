package com.bootjpabase.api.sample.domain.dto.request;

import com.bootjpabase.api.sample.domain.entity.Sample;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Sample}
 */
@Schema(description = "샘플 삭제 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class SampleDeleteRequestDTO {

    @Schema(description = "샘플 순번", example = "1")
    private Long sampleSn;
}
