package com.bootjpabase.carmanager.global.validate.common;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

/**
 * validation 적용 시 list의 경우는 @valid, @validate가 적용되지 않기 때문에 별도로 validator를 만든 뒤 적용해줘야 한다.
 */
public class CustomCollectionValidator implements Validator {

    private final Validator validator;

    public CustomCollectionValidator(LocalValidatorFactoryBean validatorFactory) {
        this.validator = validatorFactory;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {

        if(target instanceof List) {
            List<Object> collection = (List<Object>) target;
            for(Object object : collection) {
                ValidationUtils.invokeValidator(validator, object, errors);
            }
        }
    }
}