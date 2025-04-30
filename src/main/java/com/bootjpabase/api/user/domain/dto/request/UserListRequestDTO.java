package com.bootjpabase.api.user.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "사용자 목록 조회 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class UserListRequestDTO {

    @Length(max = 50, message = "이름은 50자 이하로 입력해야 합니다.")
    @Schema(description = "이름", example = "홍길동")
    private String userName;
}
