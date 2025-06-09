package com.bootjpabase.api.car.domain.entity;

import com.bootjpabase.api.car.domain.dto.request.CarUpdateRequestDTO;
import com.bootjpabase.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Comment;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_CAR")
public class Car extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_SN", nullable = false, updatable = false)
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

    @Builder.Default
    @Column(name = "RENTAL_YN", nullable = false)
    @Comment("대여 가능 여부(N: 불가능, Y: 가능)")
    private String rentalYn = "Y";

    @Column(name = "RENTAL_DESCRIPTION")
    @Comment("대여 상세 내용")
    private String rentalDescription;

    /**
     * 수정요청된 dto 값을 받아서 entity 영속성 컨텍스트를 수정한다.
     * <br> 수정할 값이 있는 데이터만 수정
     *
     * @param dto 수정요청된 Car dto
     */
    public void updateCarInfo(CarUpdateRequestDTO dto) {
        if (StringUtils.isNotBlank(dto.getCategory())) this.category = dto.getCategory(); // 카테고리
        if (StringUtils.isNotBlank(dto.getRentalYn())) this.rentalYn = dto.getRentalYn(); // 대여 가능 여부
        if (StringUtils.isNotBlank(dto.getRentalDescription()))
            this.rentalDescription = dto.getRentalDescription(); // 대여 상세 내용
    }
}
