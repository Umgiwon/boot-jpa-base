package com.bootjpabase.api.user.domain.dto.response;

import com.bootjpabase.api.user.domain.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
@Schema(description = "사용자 응답 DTO")
@Builder
@NoArgsConstructor
@Data
public class UserResponseDTO implements Serializable {

    @Schema(description = "사용자 순번", example = "1")
    private Long userSn;

    @Schema(description = "아이디", example = "user")
    private String userId;

    @Schema(description = "이름", example = "홍길동")
    private String userName;

    @Schema(description = "전화번호", example = "01011112222")
    private String userPhone;

    @Schema(description = "이메일", example = "test@test.com")
    private String userEmail;

    @Schema(description = "등록자", example = "user")
    private String createdUser;

    @Schema(description = "등록일", example = "2025-02-21 17:56:25")
    private LocalDateTime createdDate;

    @QueryProjection
    public UserResponseDTO(Long userSn, String userId, String userName, String userPhone, String userEmail, String createdUser, LocalDateTime createdDate) {
        this.userSn = userSn;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.createdUser = createdUser;
        this.createdDate = createdDate;
    }
}
