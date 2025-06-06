package com.bootjpabase.global.annotation.file;

import com.bootjpabase.global.validate.custom.file.CustomNotEmptyFileValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomNotEmptyFileValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNotEmptyFile {
    String message() default "파일을 업로드해야 합니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
