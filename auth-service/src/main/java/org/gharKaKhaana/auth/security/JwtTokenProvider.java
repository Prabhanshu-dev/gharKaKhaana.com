package org.gharKaKhaana.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.gharKaKhaana.auth.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JwtTokenProvider — JWT generation and validation.
 *
 * Token payload (claims):
 *   sub   → userId (String representation of Long)
 *   email → user's email address
 *   role  → "CUSTOMER" or "VENDOR"
 *   iat   → issued-at timestamp
 *   exp   → expiry timestamp
 *
 * Signing algorithm: HMAC-SHA256 (HS256)
 * Secret: injected from environment variable JWT_SECRET (min 256 bits recommended)
 *
 * The api-gateway uses the SAME secret to validate tokens without calling auth-service.
 * Both services must share the same JWT_SECRET environment variable value.
 */
@Component
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Generate a signed JWT for the given user.
     *
     * @param user authenticated User entity
     * @return compact JWT string
     */
    public String generateToken(User user) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate a JWT token string.
     * Returns false on any parsing or signature failure — never throws.
     */
    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extract all claims from a valid token.
     */
    public Claims extractClaims(String token) {
        return parseToken(token);
    }

    public long getExpirationMillis() {
        return expirationMs;
    }

    // ── Private Helpers ────────────────────────────────────────────────

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
