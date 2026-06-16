package org.gharKaKhaana.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JWT Authentication Global Pre-Filter
 *
 * Intercepts ALL inbound requests before routing.
 * - Public paths (e.g. /api/auth/**) are whitelisted and pass through.
 * - All other paths must carry a valid Bearer JWT in Authorization header.
 * - On success: decodes userId and role, injects as downstream headers:
 *     X-Auth-User-Id   → used by downstream services to identify the caller
 *     X-Auth-Role      → used by downstream services for role-based access
 * - On failure: returns 401 Unauthorized immediately (no routing occurs).
 *
 * Phase 2: Add rate limiting, request logging, and tracing headers here.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${gateway.jwt.secret}")
    private String jwtSecret;

    /**
     * Paths that bypass JWT validation entirely.
     * - /api/auth/register — new user signup
     * - /api/auth/login    — credential exchange for JWT
     */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // ── Whitelist public auth endpoints ──────────────────────────
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // ── Extract Authorization header ──────────────────────────────
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorizedResponse(exchange, "Missing or malformed Authorization header");
        }

        String token = authHeader.substring(7);

        // ── Validate and parse JWT ─────────────────────────────────────
        try {
            Claims claims = parseToken(token);

            String userId = claims.getSubject();
            String role   = claims.get("role", String.class);

            // ── Mutate request: inject identity headers for downstream ──
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Auth-User-Id", userId)
                    .header("X-Auth-Role", role)
                    // Strip original Authorization header from downstream request
                    // (downstream services do NOT re-validate JWT)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            return unauthorizedResponse(exchange, "Invalid or expired JWT token");
        }
    }

    /**
     * Ensures this filter runs FIRST (lowest order = highest priority).
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    // ── Private Helpers ────────────────────────────────────────────────

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String reason) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("X-Auth-Error", reason);
        return response.setComplete();
    }
}
