package com.bootjpabase.global.exception;


import com.bootjpabase.global.enums.common.ApiReturnCode;

/**
 * 데이터 중복시 처리할 Custom Exception
 */
public class DataConflictException extends BusinessException {

    public DataConflictException(Object data) {
        super(ApiReturnCode.DATA_CONFLICT_ERROR,
                String.format("%s: [%s]"
                        , ApiReturnCode.DATA_CONFLICT_ERROR.getMessage()
                        , data
                )
        );
    }
}
