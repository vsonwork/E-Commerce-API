package com.ecommerce.auth;

import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.Set;

public class TokenUtils {
    public static String generateToken(String username, String role) {
        return Jwt.issuer("ecommerce-app")
                .upn(username)
                .groups(Set.of(role))
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}