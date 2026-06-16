package org.gharKaKhaana.auth.common.exception;

/**
 * Thrown when a registration attempt uses an email that is already registered.
 * Mapped to HTTP 409 Conflict by GlobalExceptionHandler.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
