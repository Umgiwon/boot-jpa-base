package com.bootjpabase.global.enums.car;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RentalYn {
    RENTAL_Y("Y"),
    RENTAL_N("N");

    public static final String TYPE_NAME = "대여 가능 여부";

    private final String value;
}
