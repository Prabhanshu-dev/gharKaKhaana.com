package org.gharKaKhaana.vendor.common;

import org.gharKaKhaana.vendor.common.exception.UnauthorizedVendorAccessException;
import org.gharKaKhaana.vendor.common.exception.VendorNotFoundException;
import org.gharKaKhaana.vendor.common.exception.VendorProfileAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler — Centralized exception-to-HTTP-status mapping for vendor-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 Not Found */
    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(VendorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /** 409 Conflict — duplicate profile */
    @ExceptionHandler(VendorProfileAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(VendorProfileAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /** 403 Forbidden — ownership violation */
    @ExceptionHandler(UnauthorizedVendorAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(UnauthorizedVendorAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /** 403 Forbidden — role enforcement (CUSTOMER calling VENDOR endpoint) */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.error(ex.getReason()));
    }

    /** 400 Bad Request — @Valid violations */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e ->
                errors.put(((FieldError) e).getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed.")
                        .data(errors)
                        .build());
    }

    /** 500 Internal Server Error — fallback */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An internal server error occurred."));
    }
}
