package com.bootjpabase.api.manager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "관리자 목록 조회 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class ManagerLoginRequestDTO {

    @NotBlank(message = "아이디는 필수입니다")
    @Length(max = 30, message = "아이디는 30자 이하로 입력해야 합니다.")
    @Schema(description = "아이디", example = "admin")
    private String id;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Length(max = 30, message = "비밀번호는 30자 이하로 입력해야 합니다.")
    @Schema(description = "비밀번호", example = "admin!")
    private String password;
}
