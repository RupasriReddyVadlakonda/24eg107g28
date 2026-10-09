package com.datavault.personal_data_vault.exception;

import com.datavault.personal_data_vault.dto.response.ApiResponse;
import com.datavault.personal_data_vault.exception.ConsentAccessDeniedException;
import com.datavault.personal_data_vault.exception.DuplicateResourceException;
import com.datavault.personal_data_vault.exception.ResourceNotFoundException;
import com.datavault.personal_data_vault.exception.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(), "RESOURCE_NOT_FOUND"));
    }

    @ExceptionHandler(value={DuplicateResourceException.class})
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage(), "DUPLICATE_RESOURCE"));
    }

    @ExceptionHandler(value={ConsentAccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleConsentDenied(ConsentAccessDeniedException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.FORBIDDEN).body(ApiResponse.error(ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.FORBIDDEN).body(ApiResponse.error("Access denied", "ACCESS_DENIED"));
    }

    @ExceptionHandler(value={UnauthorizedException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body(ApiResponse.error(ex.getMessage(), "UNAUTHORIZED"));
    }

    @ExceptionHandler(value={BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Invalid credentials", "AUTHENTICATION_FAILED"));
    }

    @ExceptionHandler(value={AuthenticationException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Authentication failed", "AUTHENTICATION_FAILED"));
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(ApiResponse.error(message, "VALIDATION_FAILED"));
    }

    @ExceptionHandler(value={ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(Exception ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error("Request parameter is invalid", "VALIDATION_FAILED"));
    }

    @ExceptionHandler(value={HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error("Request body is invalid", "INVALID_REQUEST_BODY"));
    }

    @ExceptionHandler(value={IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage(), "INVALID_REQUEST"));
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR"));
    }
}

