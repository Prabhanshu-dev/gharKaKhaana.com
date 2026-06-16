package org.gharKaKhaana.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User — Core identity entity for the platform.
 *
 * Refactored from the original Customer.java.
 * Key design changes:
 *   - Renamed to User (Customer was misleading; Vendors are users too)
 *   - Added Role enum (CUSTOMER / VENDOR) — replaces hard-coded role assumptions
 *   - Password is NEVER returned in API responses (see AuthResponse DTO)
 *   - Added isActive flag for soft-disable support
 *   - Added createdAt timestamp
 *   - phoneNumber retained from original Customer entity
 *
 * Stored in: gkk_auth_db.users
 * Cross-service reference: other services reference this entity by userId (Long)
 * as a logical FK — no cross-DB foreign key constraints.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * BCrypt-hashed password.
     * Raw password is NEVER stored or logged.
     * Hashed in AuthServiceImpl before save — PasswordEncoder injected there.
     */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 15)
    private String phoneNumber;

    /**
     * Account role — determines routing and access control across services.
     * Stored as string enum (e.g. "CUSTOMER", "VENDOR").
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * Soft-disable flag. Inactive users cannot authenticate.
     * Default: true (active on creation).
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
