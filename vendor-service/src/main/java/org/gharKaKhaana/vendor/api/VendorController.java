package org.gharKaKhaana.vendor.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.vendor.application.VendorService;
import org.gharKaKhaana.vendor.application.dto.VendorProfileRequest;
import org.gharKaKhaana.vendor.application.dto.VendorProfileResponse;
import org.gharKaKhaana.vendor.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * VendorController — REST endpoints for vendor profile management.
 *
 * Auth contract:
 *   All requests arriving here have already been JWT-validated by the api-gateway.
 *   The gateway strips the Authorization header and injects:
 *     X-Auth-User-Id  — authenticated user's DB id (Long as string)
 *     X-Auth-Role     — "CUSTOMER" or "VENDOR"
 *
 *   Write endpoints (POST, PUT) additionally check that the role is VENDOR.
 *
 * Endpoints:
 *   GET  /api/vendors                 — public, paginated list (size=20)
 *   GET  /api/vendors/{id}            — public, single vendor by id
 *   GET  /api/vendors/me              — authenticated vendor's own profile
 *   POST /api/vendors/profile         — create profile (VENDOR only)
 *   PUT  /api/vendors/profile/{id}    — update profile (VENDOR only, owner only)
 */
@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private final VendorService vendorService;

    /**
     * Public — list all active, verified vendors.
     * Paginated with default size=20, sorted by rating descending.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<VendorProfileResponse>>> listVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<VendorProfileResponse> vendors = vendorService.listActiveVendors(pageable);
        return ResponseEntity.ok(ApiResponse.success("Vendors retrieved successfully.", vendors));
    }

    /**
     * Public — fetch a single vendor by vendorId.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorProfileResponse>> getVendorById(
            @PathVariable Long id) {

        VendorProfileResponse vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(ApiResponse.success("Vendor profile retrieved.", vendor));
    }

    /**
     * Authenticated VENDOR — fetch own profile.
     * userId resolved from X-Auth-User-Id header (gateway-injected).
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<VendorProfileResponse>> getMyProfile(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role) {

        enforceVendorRole(role);
        VendorProfileResponse profile = vendorService.getMyProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Your vendor profile.", profile));
    }

    /**
     * Authenticated VENDOR — create kitchen profile.
     * userId sourced from X-Auth-User-Id header — never from request body.
     */
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<VendorProfileResponse>> createProfile(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody VendorProfileRequest request) {

        enforceVendorRole(role);
        VendorProfileResponse created = vendorService.createProfile(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vendor profile created successfully.", created));
    }

    /**
     * Authenticated VENDOR — update own kitchen profile.
     * Ownership is verified in service layer (userId must match profile's userId).
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<VendorProfileResponse>> updateProfile(
            @PathVariable Long id,
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody VendorProfileRequest request) {

        enforceVendorRole(role);
        VendorProfileResponse updated = vendorService.updateProfile(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Vendor profile updated successfully.", updated));
    }

    // ── Private Helpers ────────────────────────────────────────────────

    /**
     * Enforces that the caller has VENDOR role.
     * Called on all write endpoints — prevents CUSTOMER accounts from
     * creating vendor profiles even with a valid JWT.
     */
    private void enforceVendorRole(String role) {
        if (!"VENDOR".equalsIgnoreCase(role)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "Access denied. Only VENDOR accounts can perform this action."
            );
        }
    }
}
