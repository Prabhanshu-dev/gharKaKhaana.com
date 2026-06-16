package org.gharKaKhaana.auth.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.auth.application.dto.AuthResponse;
import org.gharKaKhaana.auth.application.dto.LoginRequest;
import org.gharKaKhaana.auth.application.dto.RegisterRequest;
import org.gharKaKhaana.auth.common.exception.EmailAlreadyExistsException;
import org.gharKaKhaana.auth.common.exception.InvalidCredentialsException;
import org.gharKaKhaana.auth.domain.User;
import org.gharKaKhaana.auth.infrastructure.UserRepository;
import org.gharKaKhaana.auth.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthServiceImpl — Authentication use-case implementation.
 *
 * Coordinates: UserRepository (data) + PasswordEncoder (BCrypt) + JwtTokenProvider (tokens).
 *
 * Security guarantees:
 *   - Passwords are BCrypt-hashed BEFORE any persistence call.
 *   - Raw passwords are never stored, logged, or returned in any response.
 *   - Login failure returns a generic error to prevent user enumeration attacks.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Guard: reject duplicate email registrations
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "An account with email '" + request.getEmail() + "' already exists."
            );
        }

        // Build and persist the user entity with BCrypt-hashed password
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt hash
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        // Issue JWT immediately — client is authenticated upon registration
        String token = jwtTokenProvider.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .expiresIn(jwtTokenProvider.getExpirationMillis())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Lookup by email — generic error on failure (prevents user enumeration)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        // Reject inactive accounts
        if (!user.isActive()) {
            throw new InvalidCredentialsException("This account has been deactivated.");
        }

        // BCrypt comparison — raw password never stored
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String token = jwtTokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .expiresIn(jwtTokenProvider.getExpirationMillis())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.isTokenValid(token);
    }
}
