package com.bootjpabase.api.user.domain.entity;


import com.bootjpabase.global.config.jwt.domain.entity.RefreshToken;
import com.bootjpabase.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_USER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SN", updatable = false, nullable = false)
    @Comment("사용자 순번")
    private Long userSn;

    @Column(name = "USER_ID", length = 30, updatable = false, nullable = false)
    @Comment("사용자 아이디")
    private String userId;

    @Column(name = "USER_NAME", length = 50, nullable = false)
    @Comment("사용자 이름")
    private String userName;

    @Column(name = "USER_PASSWORD", length = 100, nullable = false)
    @Comment("사용자 비밀번호")
    private String userPassword;

    @Column(name = "USER_PHONE", length = 11, nullable = false)
    @Comment("사용자 전화번호")
    private String userPhone;

    @Column(name = "USER_EMAIL", length = 200, nullable = false)
    @Comment("사용자 이메일")
    private String userEmail;

    @Column(name = "PROFILE_IMG_FILE_SN")
    @Comment("프로필 파일순번")
    private Long profileImgFileSn;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;
}
