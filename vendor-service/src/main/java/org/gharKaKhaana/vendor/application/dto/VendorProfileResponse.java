package org.gharKaKhaana.vendor.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * VendorProfileResponse — outbound DTO for vendor profile data.
 *
 * Returned by GET /api/vendors, GET /api/vendors/{id}, POST /api/vendors/profile.
 * Internal entity fields (e.g., @PrePersist hooks) are not exposed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorProfileResponse {

    private Long id;
    private Long userId;
    private String kitchenName;
    private String ownerName;
    private String address;
    private String phone;
    private Double rating;
    private boolean isVerified;
    private boolean isActive;
    private LocalDateTime createdAt;
}
