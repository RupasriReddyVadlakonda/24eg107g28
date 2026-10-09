package com.datavault.personal_data_vault.exception;

public class ResourceNotFoundException
extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

