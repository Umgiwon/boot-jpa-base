package com.bootjpabase.carenation.api.car.domain.entity;

import com.bootjpabase.carenation.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "TB_CAR")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Car extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_SN", updatable = false, nullable = false)
    @Comment("자동차 순번")
    private Long carSn;

    @Column(name = "CATEGORY", nullable = false)
    @Comment("카테고리")
    private String category;

    @Column(name = "MANUFACTURER", nullable = false)
    @Comment("제조사")
    private String manufacturer;

    @Column(name = "MODEL_NAME", nullable = false)
    @Comment("모델명")
    private String modelName;

    @Column(name = "PRODUCTION_YEAR", nullable = false)
    @Comment("생산년도")
    private Integer productionYear;

    @Column(name = "RENTAL_YN", nullable = false)
    @Comment("대여 가능 여부(N: 불가능, Y: 가능)")
    private String rentalYn;

    @Column(name = "RENTAL_DESCRIPTION")
    @Comment("대여 상세 내용")
    private String rentalDescription;
}
