package com.bootjpabase.carmanager.global.exception;

import com.bootjpabase.carmanager.global.enums.ApiReturnCode;
import com.bootjpabase.carmanager.global.validate.common.CustomCollectionValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final String ERROR_PATH = "/error";

    /* Collection valid를 위한 로직 추가 */
    protected final LocalValidatorFactoryBean validator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new CustomCollectionValidator(validator));
    }
    /* //Collection valid를 위한 로직 추가 */

    /**
     * RuntimeException 발생시 처리 핸들러
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMsg> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(ApiReturnCode.SERVER_ERROR.getMessage())
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * IllegalAccessException 발생시 처리 핸들러
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ExceptionMsg> handleIllegalAccessException(IllegalAccessException e) {
        log.error(e.getMessage(), e);

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * request body의 argument validation 처리 핸들러 (@valid, @validated 어노테이션)
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMsg> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> "[" + error.getField() + "] " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(errorMessage)
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * Constraint Violdation Exception 처리 핸들러 (@valid, @validated 어노테이션)
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMsg> handleConstraintViolationException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")))
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * parameter 데이터 type이 일치하지 않을 때 처리 핸들러 (IllegalArgumentException의 하위)
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionMsg> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {

        String errorMessage = String.format("'%s' 값은 잘못된 형식입니다. %s 타입이어야 합니다.",
                e.getValue(), e.getRequiredType().getSimpleName());

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(errorMessage)
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * missing parameter 발생 시 처리 핸들러
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionMsg> handleMissingParams(MissingServletRequestParameterException ex) {

        ExceptionMsg exceptionMsg = ExceptionMsg.builder()
                .success(false)
                .path(ERROR_PATH)
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(ex.getParameterName() + " 파라미터는 필수값입니다.")
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }
}
