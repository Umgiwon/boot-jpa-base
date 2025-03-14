package com.bootjpabase.api.car.repository;

import com.bootjpabase.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.domain.dto.response.QCarResponseDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bootjpabase.api.car.domain.entity.QCar.car;


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
    public Page<CarResponseDTO> getCarList(CarListRequestDTO dto, Pageable pageable) {
        List<CarResponseDTO> resultList;

        resultList = queryFactory
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
                .where(pagingCondition(dto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(car.carSn.asc())
                .fetch();

        // 전체 데이터 카운트
        JPAQuery<Long> countQuery = queryFactory
                .select(car.count())
                .from(car)
                .where(pagingCondition(dto));

        return PageableExecutionUtils.getPage(resultList, pageable, countQuery::fetchOne);
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * 페이징 처리시 조건절
     * @param dto
     * @return
     */
    private BooleanBuilder pagingCondition(CarListRequestDTO dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if(dto.getCategory() != null) {
            builder.and(eqCategory(dto.getCategory()));
        }

        if(dto.getManufacturer() != null) {
            builder.and(eqManufacturer(dto.getManufacturer()));
        }

        if(dto.getModelName() != null) {
            builder.and(eqModelName(dto.getModelName()));
        }

        if(dto.getProductionYear() != null) {
            builder.and(eqProductionYear(dto.getProductionYear()));
        }

        if(dto.getRentalYn() != null) {
            builder.and(eqRentalYn(dto.getRentalYn()));
        }

        return builder;
    }

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
