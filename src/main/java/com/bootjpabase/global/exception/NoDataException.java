package com.bootjpabase.global.exception;

import com.bootjpabase.global.enums.common.ApiReturnCode;

/**
 * 데이터 없을시 처리할 Custom Exception
 */
public class NoDataException extends BusinessException {

    public NoDataException(String message) {
        super(ApiReturnCode.NO_DATA_ERROR,
                String.format("%s %s"
                        , message
                        , ApiReturnCode.NO_DATA_ERROR.getMessage()
                )
        );
    }
}
