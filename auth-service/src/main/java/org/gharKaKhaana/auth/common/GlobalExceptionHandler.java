package org.gharKaKhaana.auth.common;

import org.gharKaKhaana.auth.common.exception.EmailAlreadyExistsException;
import org.gharKaKhaana.auth.common.exception.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler — Centralized exception-to-HTTP-status mapping.
 *
 * Replaces the anti-pattern of throwing raw RuntimeException from services
 * and getting opaque HTTP 500 responses.
 *
 * All API error responses follow the ApiResponse envelope:
 *   { "success": false, "message": "...", "data": null }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 409 Conflict — duplicate email registration attempt.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailConflict(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * 401 Unauthorized — bad credentials on login.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * 400 Bad Request — @Valid constraint violations on request DTOs.
     * Returns a map of field → error message for actionable client feedback.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName    = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed. Please check the errors.")
                        .data(fieldErrors)
                        .build());
    }

    /**
     * 500 Internal Server Error — unhandled exceptions.
     * Generic message shown to client — full stack trace goes to logs only.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        // TODO: integrate with structured logging (Phase 5)
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An internal server error occurred. Please try again later."));
    }
}
