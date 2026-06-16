package org.gharKaKhaana.auth.application;

import org.gharKaKhaana.auth.application.dto.AuthResponse;
import org.gharKaKhaana.auth.application.dto.LoginRequest;
import org.gharKaKhaana.auth.application.dto.RegisterRequest;

/**
 * AuthService — Application layer port (interface).
 *
 * Defines the contract for authentication use-cases.
 * Implementation: AuthServiceImpl
 */
public interface AuthService {

    /**
     * Register a new user account (CUSTOMER or VENDOR role).
     * Hashes password with BCrypt before persistence.
     * Returns a JWT token so the client is immediately authenticated post-registration.
     *
     * @throws org.gharKaKhaana.auth.common.exception.EmailAlreadyExistsException if email is taken
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate an existing user by email + password.
     * Returns a signed JWT token on success.
     *
     * @throws org.gharKaKhaana.auth.common.exception.InvalidCredentialsException on failure
     */
    AuthResponse login(LoginRequest request);

    /**
     * Validate a JWT token string.
     * Called internally by the api-gateway GlobalFilter.
     * Returns true if token is valid and not expired.
     */
    boolean validateToken(String token);
}
