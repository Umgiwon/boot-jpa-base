package com.bootjpabase.carenation.global.exception;

import com.bootjpabase.carenation.global.enums.ApiReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final String ERROR_PATH = "/error";

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
                .timestamp(LocalDateTime.now())
                .path(ERROR_PATH)
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
                .timestamp(LocalDateTime.now())
                .path(ERROR_PATH)
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(ex.getParameterName() + " 파라미터는 필수값입니다.")
                .build();

        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }
}
