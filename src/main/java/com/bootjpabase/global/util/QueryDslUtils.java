package com.bootjpabase.global.util;

import com.bootjpabase.api.user.domain.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
                System.out.println("정렬 불가 필드: " + order.getProperty());
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}
