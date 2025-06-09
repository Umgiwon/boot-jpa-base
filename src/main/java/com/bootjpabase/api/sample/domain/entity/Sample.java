package com.bootjpabase.api.sample.domain.entity;

import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Comment;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Builder를 위한 생성자는 private
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 기본 생성자는 protected
@Getter
@Entity
@Table(name = "TB_SAMPLE")
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAMPLE_SN", nullable = false, updatable = false)
    @Comment("샘플 순번")
    private Long sampleSn;

    @Column(name = "TITLE", nullable = false, length = 50)
    @Comment("샘플 제목")
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 100)
    @Comment("샘플 내용")
    private String content;

    /**
     * 수정요청된 dto 값을 받아서 entity 영속성 컨텍스트를 수정한다.
     * <p> 수정할 값이 있는 데이터만 수정
     *
     * @param dto 수정요청된 Sample dto
     */
    public void updateSampleInfo(SampleUpdateRequestDTO dto) {
        if (StringUtils.isNotBlank(dto.getTitle())) this.title = dto.getTitle(); // 제목
        if (StringUtils.isNotBlank(dto.getContent())) this.content = dto.getContent(); // 내용
    }
}
