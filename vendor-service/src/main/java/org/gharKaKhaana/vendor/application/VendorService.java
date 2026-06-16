package org.gharKaKhaana.vendor.application;

import org.gharKaKhaana.vendor.application.dto.VendorProfileRequest;
import org.gharKaKhaana.vendor.application.dto.VendorProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * VendorService — Application layer port (interface).
 */
public interface VendorService {

    /**
     * Create a new vendor profile linked to the given userId.
     * userId is sourced from the X-Auth-User-Id header — never from the request body.
     *
     * @throws org.gharKaKhaana.vendor.common.exception.VendorProfileAlreadyExistsException
     *         if a profile already exists for this userId
     */
    VendorProfileResponse createProfile(Long userId, VendorProfileRequest request);

    /**
     * Update an existing vendor profile.
     * Only the profile owner (matching userId) may update.
     *
     * @throws org.gharKaKhaana.vendor.common.exception.VendorNotFoundException if not found
     * @throws org.gharKaKhaana.vendor.common.exception.UnauthorizedVendorAccessException if userId mismatch
     */
    VendorProfileResponse updateProfile(Long vendorId, Long userId, VendorProfileRequest request);

    /**
     * Fetch a vendor profile by its primary key (vendorId).
     */
    VendorProfileResponse getVendorById(Long vendorId);

    /**
     * Fetch the vendor profile belonging to the currently authenticated user.
     */
    VendorProfileResponse getMyProfile(Long userId);

    /**
     * List all active + verified vendors — paginated.
     * Default page size enforced by controller: size=20.
     */
    Page<VendorProfileResponse> listActiveVendors(Pageable pageable);
}
