package org.gharKaKhaana.auth.infrastructure;

import org.gharKaKhaana.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository — Data access layer for the User entity.
 *
 * Refactored from CustomerRepository.java.
 * Key changes:
 *   - Removed cross-domain queries (Items / Orders) — those belong in their own services.
 *   - Raw @Query replaced with Spring Data derived method names (cleaner, type-safe).
 *   - findByUsernameAndPassword removed — password comparison is done in service layer
 *     via BCrypt (never via a raw DB query).
 *
 * Stored in: gkk_auth_db
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique email address.
     * Used for login lookup and duplicate registration check.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if an email is already registered.
     * Used during registration to return a clear conflict error.
     */
    boolean existsByEmail(String email);
}
