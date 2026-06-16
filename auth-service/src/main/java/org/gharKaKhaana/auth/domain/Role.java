package org.gharKaKhaana.auth.domain;

/**
 * Role — User account role enum.
 *
 * CUSTOMER : End-user placing food orders.
 * VENDOR   : Home-cook managing a kitchen profile (linked in vendor-service).
 *
 * Stored as a STRING column in gkk_auth_db.users.role.
 * When a VENDOR registers here, the vendor-service creates the kitchen profile
 * asynchronously via the X-Auth-User-Id header forwarded by the api-gateway.
 */
public enum Role {
    CUSTOMER,
    VENDOR
}
