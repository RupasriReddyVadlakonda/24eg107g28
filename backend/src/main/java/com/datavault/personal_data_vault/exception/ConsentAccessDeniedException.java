package com.datavault.personal_data_vault.exception;

public class ConsentAccessDeniedException
extends RuntimeException {
    private final String errorCode;

    public ConsentAccessDeniedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}

