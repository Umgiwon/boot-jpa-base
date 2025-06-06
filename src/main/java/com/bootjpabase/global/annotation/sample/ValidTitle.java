package com.bootjpabase.global.annotation.sample;

import com.bootjpabase.global.validate.custom.sample.CustomTitleValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomTitleValidator.class)
public @interface ValidTitle {
    String message() default "제목은 30글자 이하로 입력해야 합니다";

    Class[] groups() default {};

    Class[] payload() default {};
}
