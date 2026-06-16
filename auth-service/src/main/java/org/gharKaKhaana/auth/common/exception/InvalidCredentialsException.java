package org.gharKaKhaana.auth.common.exception;

/**
 * Thrown when login credentials are invalid (wrong email or wrong password).
 * Uses a deliberately generic message to prevent user enumeration attacks.
 * Mapped to HTTP 401 Unauthorized by GlobalExceptionHandler.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
