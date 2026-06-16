package org.gharKaKhaana.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.auth.application.AuthService;
import org.gharKaKhaana.auth.application.dto.AuthResponse;
import org.gharKaKhaana.auth.application.dto.LoginRequest;
import org.gharKaKhaana.auth.application.dto.RegisterRequest;
import org.gharKaKhaana.auth.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — REST endpoints for authentication.
 *
 * All endpoints are PUBLIC (no JWT required).
 * These paths are whitelisted in the api-gateway JwtAuthenticationFilter.
 *
 * Endpoints:
 *   POST /api/auth/register  → create account, returns JWT
 *   POST /api/auth/login     → authenticate, returns JWT
 *   GET  /api/auth/validate  → token validity check (for gateway introspection)
 *
 * Response wrapper: ApiResponse<T> — consistent {status, message, data} envelope
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user account (CUSTOMER or VENDOR).
     * Returns HTTP 201 Created on success.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully.", authResponse));
    }

    /**
     * Authenticate with email + password.
     * Returns HTTP 200 OK with JWT on success.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful.", authResponse));
    }

    /**
     * Validate a JWT token string.
     * Called internally by the api-gateway — not intended for direct client use.
     * Returns HTTP 200 with valid=true/false.
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validate(
            @RequestParam String token) {

        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success("Token validation result.", isValid));
    }
}
