package org.gharKaKhaana.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.gharKaKhaana.auth.domain.Role;

/**
 * RegisterRequest — inbound DTO for POST /api/auth/register
 *
 * Validated by Spring's @Valid — controller never touches raw request params.
 * Password is accepted here as plaintext and BCrypt-hashed in AuthServiceImpl
 * before any persistence occurs.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    /**
     * Plaintext password from client — hashed before storage, never logged.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Size(max = 15, message = "Phone number too long")
    private String phoneNumber;

    /**
     * Account role — client must specify CUSTOMER or VENDOR at registration.
     * VENDOR registration creates the base account here; vendor-service
     * handles the kitchen profile creation separately.
     */
    @NotNull(message = "Role is required (CUSTOMER or VENDOR)")
    private Role role;
}
