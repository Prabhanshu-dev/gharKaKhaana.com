package org.gharKaKhaana.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginRequest — inbound DTO for POST /api/auth/login
 *
 * Credentials are validated against the database via BCrypt comparison.
 * Raw password is NEVER stored or logged — used only for BCrypt.matches().
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
