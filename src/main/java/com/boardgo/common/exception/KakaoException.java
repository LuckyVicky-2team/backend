package com.boardgo.common.exception;

import com.boardgo.common.exception.advice.dto.ErrorCode;

public class KakaoException extends ExternalException {

    public KakaoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public KakaoException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
