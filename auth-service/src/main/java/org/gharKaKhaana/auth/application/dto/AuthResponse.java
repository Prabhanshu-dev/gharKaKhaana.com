package org.gharKaKhaana.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gharKaKhaana.auth.domain.Role;

/**
 * AuthResponse — outbound DTO for POST /api/auth/register and POST /api/auth/login
 *
 * IMPORTANT: password is deliberately excluded from this response.
 * The JWT token is the only credential returned to the client.
 * The client stores this token and sends it as: Authorization: Bearer <token>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** Signed JWT — contains sub (userId), email, role, iat, exp */
    private String token;

    private Long userId;
    private String name;
    private String email;
    private Role role;

    /** Token expiry duration in milliseconds (informational for client) */
    private long expiresIn;
}
