package com.bootjpabase.global.config.jwt.domain.entity;

import com.bootjpabase.api.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_SN", updatable = false, nullable = false)
    @Comment("토큰 순번")
    private Long tokenSn;

    @Column(name = "TOKEN", nullable = false, unique = true)
    @Comment("토큰 값")
    private String token;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_SN", referencedColumnName = "USER_SN", nullable = false)
    @Comment("사용자")
    private User user;
}
