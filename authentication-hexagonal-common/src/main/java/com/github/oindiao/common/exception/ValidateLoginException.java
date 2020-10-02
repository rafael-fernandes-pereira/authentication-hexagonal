package com.github.oindiao.common.exception;

public class ValidateLoginException extends RuntimeException {

    public ValidateLoginException(String message, Throwable error) {
        super(message, error);
    }
}
