package com.bootjpabase.global.validate.custom.sample;

import com.bootjpabase.global.annotation.sample.ValidContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomContentValidator implements ConstraintValidator<ValidContent, String> {

    @Override
    public boolean isValid(String content, ConstraintValidatorContext context) {
        return !content.isEmpty() && content.length() <= 150;
    }
}
