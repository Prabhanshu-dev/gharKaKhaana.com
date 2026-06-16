package org.gharKaKhaana.vendor.infrastructure;

import org.gharKaKhaana.vendor.domain.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * VendorRepository — Data access for the Vendor entity.
 *
 * Stored in: gkk_vendor_db.vendors
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    /**
     * Find a vendor profile by the auth-service userId.
     * Used to fetch or update the profile of the currently authenticated vendor.
     */
    Optional<Vendor> findByUserId(Long userId);

    /**
     * Check if a vendor profile already exists for a given userId.
     * Prevents duplicate profile creation on repeated POST calls.
     */
    boolean existsByUserId(Long userId);

    /**
     * Fetch all active, verified vendors — paginated (default size=20).
     * Used by the public vendor listing endpoint.
     */
    Page<Vendor> findByIsActiveTrueAndIsVerifiedTrue(Pageable pageable);
}
