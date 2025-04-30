package com.bootjpabase.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * * QueryDSL 사용 시 관련 util
 */
@UtilityClass
@Slf4j
public class QueryDslUtils {

    /** V1
     * queryDsl 페이징 처리 시 정렬 정보 가져와서 return
     * @param pageable
     * @param entityPath
     * @return
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, PathBuilder<?> entityPath) {

        return pageable.getSort().stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                    ComparableExpressionBase<?> expression = entityPath.getComparable(order.getProperty(), Comparable.class);
                    return new OrderSpecifier<>(direction, expression);
                }).toArray(OrderSpecifier<?>[]::new);
    }

    /** V2
     * queryDsl 페이징 처리 시 정렬 정보 가져와서 return
     * ** 작동은 이상 없으나 맞지않는 컬럼 보낼 경우 에러발생
     * *** 사용법) .orderBy(QueryDslUtils.getOrderSpecifiers2(pageable, Sample.class, "sample"))
     *
     * @param pageable
     * @param clazz
     * @param alias
     * @return
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers2(Pageable pageable, Class<?> clazz, String alias) {
        PathBuilder<?> entityPath = new PathBuilder<>(clazz, alias);
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            try {
                // 경로를 Comparable로 명시적 캐스팅
                orders.add(new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        entityPath.getComparable(order.getProperty(), Comparable.class)
                ));
            } catch (IllegalArgumentException e) {
                // 잘못된 필드는 무시하거나 로깅
                log.error("정렬 불가 필드: {}", order.getProperty());
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }
}
