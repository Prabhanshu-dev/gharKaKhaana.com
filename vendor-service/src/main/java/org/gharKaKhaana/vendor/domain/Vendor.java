package org.gharKaKhaana.vendor.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Vendor — Kitchen/home-cook profile entity.
 *
 * Links to a User in auth-service via userId (logical FK — no cross-DB constraint).
 * One User with role=VENDOR maps to exactly ONE Vendor profile.
 *
 * Stored in: gkk_vendor_db.vendors
 *
 * Field notes:
 *   - userId       : Long referencing gkk_auth_db.users.id (logical, not a DB FK)
 *   - kitchenName  : Public-facing name of the home kitchen (e.g. "Maa Ki Rasoi")
 *   - isVerified   : Admin-verified flag — unverified vendors cannot list on menu-service
 *   - rating       : Computed average rating (updated by order/review service in future)
 */
@Entity
@Table(name = "vendors", indexes = {
        @Index(name = "idx_vendors_user_id", columnList = "userId", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Logical reference to gkk_auth_db.users.id.
     * Unique — one vendor profile per user account.
     * Populated from the X-Auth-User-Id header on profile creation.
     */
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 150)
    private String kitchenName;

    @Column(nullable = false, length = 100)
    private String ownerName;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(length = 15)
    private String phone;

    /**
     * Average customer rating (0.0 – 5.0).
     * Defaulted to 0.0 on creation — updated as orders are reviewed.
     */
    @Builder.Default
    @Column(nullable = false)
    private Double rating = 0.0;

    /**
     * Admin verification flag.
     * Vendors must be verified before their menu items are visible on the platform.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean isVerified = false;

    /**
     * Active flag — soft-disable without deletion.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
