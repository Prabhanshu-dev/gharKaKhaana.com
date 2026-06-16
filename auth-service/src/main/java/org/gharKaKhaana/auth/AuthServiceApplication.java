package org.gharKaKhaana.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GharKaKhaana Auth Service
 *
 * Sole identity provider for the entire platform.
 * Issues JWT tokens consumed by the API Gateway and all downstream services.
 *
 * Public endpoints (no JWT required — whitelisted in api-gateway):
 *   POST /api/auth/register  — account creation (CUSTOMER or VENDOR role)
 *   POST /api/auth/login     — credential exchange → JWT
 *   GET  /api/auth/validate  — token introspection (called by api-gateway)
 */
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
