package org.gharKaKhaana.vendor.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.vendor.application.dto.VendorProfileRequest;
import org.gharKaKhaana.vendor.application.dto.VendorProfileResponse;
import org.gharKaKhaana.vendor.common.exception.UnauthorizedVendorAccessException;
import org.gharKaKhaana.vendor.common.exception.VendorNotFoundException;
import org.gharKaKhaana.vendor.common.exception.VendorProfileAlreadyExistsException;
import org.gharKaKhaana.vendor.domain.Vendor;
import org.gharKaKhaana.vendor.infrastructure.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * VendorServiceImpl — Vendor profile use-case implementation.
 *
 * Security contract:
 *   - userId is ALWAYS sourced from the X-Auth-User-Id header (injected by api-gateway).
 *   - It is NEVER read from the request body to prevent impersonation.
 *   - Ownership is verified on update: requesting userId must match profile's userId.
 */
@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public VendorProfileResponse createProfile(Long userId, VendorProfileRequest request) {
        if (vendorRepository.existsByUserId(userId)) {
            throw new VendorProfileAlreadyExistsException(
                    "A vendor profile already exists for this account."
            );
        }

        Vendor vendor = Vendor.builder()
                .userId(userId)
                .kitchenName(request.getKitchenName())
                .ownerName(request.getOwnerName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();

        return toResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorProfileResponse updateProfile(Long vendorId, Long userId, VendorProfileRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor profile not found: " + vendorId));

        // Ownership check — only the profile owner may update
        if (!vendor.getUserId().equals(userId)) {
            throw new UnauthorizedVendorAccessException("You are not authorized to update this profile.");
        }

        vendor.setKitchenName(request.getKitchenName());
        vendor.setOwnerName(request.getOwnerName());
        vendor.setAddress(request.getAddress());
        vendor.setPhone(request.getPhone());

        return toResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorProfileResponse getVendorById(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .map(this::toResponse)
                .orElseThrow(() -> new VendorNotFoundException("Vendor profile not found: " + vendorId));
    }

    @Override
    public VendorProfileResponse getMyProfile(Long userId) {
        return vendorRepository.findByUserId(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new VendorNotFoundException(
                        "No vendor profile found for your account. Please create one first."));
    }

    @Override
    public Page<VendorProfileResponse> listActiveVendors(Pageable pageable) {
        return vendorRepository
                .findByIsActiveTrueAndIsVerifiedTrue(pageable)
                .map(this::toResponse);
    }

    // ── Mapper ─────────────────────────────────────────────────────────
    private VendorProfileResponse toResponse(Vendor vendor) {
        return VendorProfileResponse.builder()
                .id(vendor.getId())
                .userId(vendor.getUserId())
                .kitchenName(vendor.getKitchenName())
                .ownerName(vendor.getOwnerName())
                .address(vendor.getAddress())
                .phone(vendor.getPhone())
                .rating(vendor.getRating())
                .isVerified(vendor.isVerified())
                .isActive(vendor.isActive())
                .createdAt(vendor.getCreatedAt())
                .build();
    }
}
