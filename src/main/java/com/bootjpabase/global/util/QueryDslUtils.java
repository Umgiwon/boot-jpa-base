package com.bootjpabase.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class QueryDslUtils {

    /**
     * queryDsl 페이징 처리 시 정렬 정보 가져와서 return
     * @param pageable
     * @param entityPath
     * @return
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, PathBuilder<?> entityPath) {
        List<OrderSpecifier<?>> orders = pageable.getSort().stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                    ComparableExpressionBase<?> expression = entityPath.getComparable(order.getProperty(), Comparable.class);
                    return new OrderSpecifier<>(direction, expression);
                })
                .collect(Collectors.toList());

        return orders.toArray(new OrderSpecifier<?>[0]);
    }
}
