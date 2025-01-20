package com.bootjpabase.carmanager.api.car.repository;

import com.bootjpabase.carmanager.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.carmanager.api.car.domain.dto.response.QCarResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bootjpabase.carmanager.api.car.domain.entity.QCar.car;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Car 목록 조회
     * @param dto
     * @return
     */
    public List<CarResponseDTO> getCarList(CarListRequestDTO dto) {

        List<CarResponseDTO> resultList = queryFactory
                .select(
                        new QCarResponseDTO(
                                car.carSn
                                , car.category
                                , car.manufacturer
                                , car.modelName
                                , car.productionYear
                                , car.rentalYn
                                , car.rentalDescription
                        )
                )
                .from(car)
                .where(eqCategory(dto.getCategory())
                        , eqManufacturer(dto.getManufacturer())
                        , eqModelName(dto.getModelName())
                        , eqProductionYear(dto.getProductionYear())
                        , eqRentalYn(dto.getRentalYn())
                )
                .orderBy(car.carSn.asc())
                .fetch();

        return resultList;
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * Car 조회 시 카테고리 비교
     * @param category
     * @return
     */
    private BooleanExpression eqCategory(String category) {
        return (!ObjectUtils.isEmpty(category)) ? car.category.contains(category) : null;
    }

    /**
     * Car 조회 시 제조사 비교
     * @param manufacturer
     * @return
     */
    private BooleanExpression eqManufacturer(String manufacturer) {
        return (!ObjectUtils.isEmpty(manufacturer)) ? car.manufacturer.contains(manufacturer) : null;
    }

    /**
     * Car 조회 시 모델명 비교
     * @param modelName
     * @return
     */
    private BooleanExpression eqModelName(String modelName) {
        return (!ObjectUtils.isEmpty(modelName)) ? car.modelName.contains(modelName) : null;
    }


    /**
     * Car 조회 시 생산년도 비교
     * @param productionYear
     * @return
     */
    private BooleanExpression eqProductionYear(Integer productionYear) {
        return (!ObjectUtils.isEmpty(productionYear)) ? car.productionYear.eq(productionYear) : null;
    }

    /**
     * Car 조회 시 대여 가능 여부 비교
     * @param rentalYn
     * @return
     */
    private BooleanExpression eqRentalYn(String rentalYn) {
        return (!ObjectUtils.isEmpty(rentalYn)) ? car.rentalYn.eq(rentalYn) : null;
    }
}
