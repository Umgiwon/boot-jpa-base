package com.bootjpabase.global.validate.common;

/**
 * validation 적용 시 list의 경우는 @valid, @validate가 적용되지 않기 때문에 별도로 validator를 만든 뒤 적용해줘야 한다.
 *
 * 그러나 검증 결과 해당 클래스가 없어도 @valid는 잘 작동하는 것으로 확인되어 주석처리함. (2025.01.24)
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
 */