package com.boardgo.common.exception;

import static com.boardgo.common.exception.advice.dto.ErrorCode.COOKIE_NOT_FOUND;

public class CookieNotFoundException extends CustomIllegalArgumentException {
    @Override
    public int getErrorCode() {
        return super.getErrorCode();
    }

    public CookieNotFoundException() {
        super(COOKIE_NOT_FOUND.getCode(), COOKIE_NOT_FOUND.getMessage());
    }
}
