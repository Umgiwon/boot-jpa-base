package com.bootjpabase.global.util;

import com.bootjpabase.global.exception.InvalidSortFieldException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * QueryDSL 사용 시 관련 util
 */
@UtilityClass
@Slf4j
public class QueryDslUtils {

    /**
     * queryDsl 페이징 처리 시 정렬 정보 가져와서 return <br>
     * 사용법 .orderBy(QueryDslUtils.getOrderSpecifiers2(pageable, Sample.class, "sample"))
     *
     * @param pageable 페이징 정보
     * @param clazz    target 클래스
     * @param alias    target 클래스명
     * @return 정렬 정보
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, Class<?> clazz, String alias) {
        PathBuilder<?> entityPath = new PathBuilder<>(clazz, alias);
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        Set<String> entityFields = getEntityFields(clazz); // 엔티티의 필드명 Set

        for (Sort.Order order : pageable.getSort()) {
            String field = order.getProperty();

            // 엔티티에 존재하는 필드 여부 검증
            if (!entityFields.contains(field)) {
                throw new InvalidSortFieldException(field, entityFields);
            }

            try {
                // 경로를 Comparable로 명시적 캐스팅
                orders.add(new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        entityPath.getComparable(order.getProperty(), Comparable.class)
                ));
            } catch (IllegalArgumentException e) {
                log.error("정렬 불가 필드: {}", order.getProperty());
                throw new InvalidSortFieldException(field);
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

    /**
     * 엔티티의 모든 필드명을 Set으로 반환 (상속받은 필드 포함)
     *
     * @param clazz 엔티티 클래스
     * @return 필드명 Set
     */
    private static Set<String> getEntityFields(Class<?> clazz) {
        Set<String> fields = new HashSet<>();
        Class<?> currentClass = clazz;

        // 상위 클래스들을 순회하면서 필드 수집
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(
                    Arrays.stream(currentClass.getDeclaredFields())
                            .filter(field -> isValidSortField(field))
                            .map(Field::getName)
                            .collect(Collectors.toSet())
            );
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * 정렬 가능한 필드인지 검증
     *
     * @param field 검사할 필드
     * @return 정렬 가능 여부
     */
    private static boolean isValidSortField(Field field) {

        // static 필드 제외
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }

        // 컬렉션 타입 제외
        if (Collection.class.isAssignableFrom(field.getType())) {
            return false;
        }

        // private이 아닌 필드 제외
        if (!Modifier.isPrivate(field.getModifiers())) {
            return false;
        }

        return true;
    }
}
