package org.gharKaKhaana.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GharKaKhaana Vendor Service
 *
 * Manages kitchen/home-cook profiles for VENDOR-role accounts.
 *
 * Auth model:
 *   - No local JWT validation. Identity is provided by the api-gateway
 *     via forwarded headers: X-Auth-User-Id and X-Auth-Role.
 *   - Controllers extract userId from the X-Auth-User-Id header.
 *   - Role enforcement: only requests with X-Auth-Role=VENDOR are accepted
 *     on write endpoints (enforced in VendorController).
 *
 * Cross-service data boundary:
 *   - Vendor profiles are linked to auth-service Users via userId (Long).
 *   - No direct DB connection to gkk_auth_db — clean API boundary.
 */
@SpringBootApplication
public class VendorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendorServiceApplication.class, args);
    }
}
