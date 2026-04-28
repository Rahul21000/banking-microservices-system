package com.rbank.auth_service.auth;

import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${secretKey}")
    private String secretKey;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException("Secret key must not be null or empty");
        }
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm).build();
    }

    // Generate a token for a user
    public String generateToken(String username, Long customerId, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withClaim("customerId", customerId)
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
    }

    // Extract username from token
    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    // Validate token with proper logging
    public boolean validateToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (SecurityException e) {
            LOGGER.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            LOGGER.debug("Decoded JWT: {}", jwt);
            return jwt;
        } catch (JWTVerificationException e) {
            LOGGER.error("Invalid token: {}", e.getMessage());
            throw new SecurityException("Invalid or tampered token", e);
        }
    }

    // Extract expiration date
    public Date extractExpiration(String token) {
        return decodeToken(token).getExpiresAt();
    }

    // Extract roles from token
    public List<String> extractRoles(String token) {
        try {
            List<String> roles = decodeToken(token).getClaim("roles").asList(String.class);
            return roles != null ? roles : List.of();
        } catch (Exception e) {
            LOGGER.warn("Failed to extract roles from token: {}", e.getMessage());
            return List.of();
        }
    }
}
