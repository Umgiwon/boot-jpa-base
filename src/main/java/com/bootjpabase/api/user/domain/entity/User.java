package com.bootjpabase.api.user.domain.entity;


import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.global.domain.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SN", nullable = false, updatable = false)
    @Comment("사용자 순번")
    private Long userSn;

    @Column(name = "USER_ID", nullable = false, updatable = false, length = 30)
    @Comment("사용자 아이디")
    private String userId;

    @Column(name = "USER_NAME", nullable = false, length = 50)
    @Comment("사용자 이름")
    private String userName;

    @Column(name = "USER_PASSWORD", nullable = false, length = 100)
    @Comment("사용자 비밀번호")
    private String userPassword;

    @Column(name = "USER_PHONE", nullable = false, unique = true, length = 11)
    @Comment("사용자 전화번호")
    private String userPhone;

    @Column(name = "USER_EMAIL", nullable = false, unique = true, length = 200)
    @Comment("사용자 이메일")
    private String userEmail;

    @Column(name = "PROFILE_IMG_FILE_SN")
    @Comment("프로필 파일순번")
    private Long profileImgFileSn;

    /**
     * 수정요청된 dto 값을 받아서 entity 영속성 컨텍스트를 수정한다.
     * <br> 수정할 값이 있는 데이터만 수정
     *
     * @param dto              수정요청된 User dto
     * @param encodedPassword  인코딩 된 비밀번호
     * @param profileImgFileSn 이미지 파일순번
     */
    public void updateUserInfo(UserUpdateRequestDTO dto, String encodedPassword, Long profileImgFileSn) {
        if (StringUtils.isNotBlank(encodedPassword)) this.userPassword = encodedPassword; // 비밀번호(인코딩)
        if (StringUtils.isNotBlank(dto.getUserPhone())) this.userPhone = dto.getUserPhone(); // 전화번호
        if (StringUtils.isNotBlank(dto.getUserEmail())) this.userEmail = dto.getUserEmail(); // 이메일
        if (profileImgFileSn != null) this.profileImgFileSn = profileImgFileSn; // 프로필 파일순번
    }
}
