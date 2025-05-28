package com.bootjpabase.api.user.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link com.bootjpabase.api.user.domain.entity.User}
 */
@Schema(description = "사용자 로그인 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginRequestDTO {

    @NotBlank(message = "아이디는 필수입니다")
    @Length(max = 30, message = "아이디는 30자 이하로 입력해야 합니다.")
    @Schema(description = "아이디", example = "user")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Length(max = 30, message = "비밀번호는 30자 이하로 입력해야 합니다.")
    @Schema(description = "비밀번호", example = "user!")
    private String userPassword;
}
