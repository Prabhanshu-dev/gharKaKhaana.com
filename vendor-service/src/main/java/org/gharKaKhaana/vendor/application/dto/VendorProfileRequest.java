package org.gharKaKhaana.vendor.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * VendorProfileRequest — inbound DTO for creating or updating a vendor profile.
 *
 * POST /api/vendors/profile — create (userId sourced from X-Auth-User-Id header, not this body)
 * PUT  /api/vendors/profile — update
 *
 * userId is NOT accepted in the request body — it is always read from the
 * X-Auth-User-Id header injected by the api-gateway, preventing spoofing.
 */
@Data
public class VendorProfileRequest {

    @NotBlank(message = "Kitchen name is required")
    @Size(min = 2, max = 150, message = "Kitchen name must be between 2 and 150 characters")
    private String kitchenName;

    @NotBlank(message = "Owner name is required")
    @Size(min = 2, max = 100, message = "Owner name must be between 2 and 100 characters")
    private String ownerName;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 300, message = "Address must be between 10 and 300 characters")
    private String address;

    @Size(max = 15, message = "Phone number is too long")
    private String phone;
}
