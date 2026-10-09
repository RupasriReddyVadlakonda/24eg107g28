package com.datavault.personal_data_vault.exception;

public class UnauthorizedException
extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

