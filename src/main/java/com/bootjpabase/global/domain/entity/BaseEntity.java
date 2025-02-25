package com.bootjpabase.global.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    @Comment("생성일자")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column
    @Comment("수정일자")
    private LocalDateTime updateDate;
}
