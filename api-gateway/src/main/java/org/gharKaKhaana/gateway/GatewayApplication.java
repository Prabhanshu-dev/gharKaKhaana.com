package org.gharKaKhaana.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GharKaKhaana API Gateway
 *
 * Single entry point for all client traffic.
 * Routes are defined in application.yml.
 * JWT validation is handled by JwtAuthenticationFilter (GlobalFilter).
 *
 * NOTE: This service runs on Spring WebFlux (reactive) — not Spring MVC.
 *       All code in this module must use reactive types (Mono, Flux).
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
