package com.dimihris.gatewayserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeycloakRoleConverterTests {
    private final KeycloakRoleConverter converter = new KeycloakRoleConverter();

    @Test
    void mapsRealmRolesToSpringAuthorities() {
        var authorities = converter.convert(jwt(Map.of("roles", List.of("ACCOUNTS", "CARDS"))));
        assertEquals(List.of("ROLE_ACCOUNTS", "ROLE_CARDS"),
                authorities.stream().map(GrantedAuthority::getAuthority).toList());
    }

    @Test
    void missingOrMalformedRealmRolesGrantNoAuthorities() {
        var withoutRealm = Jwt.withTokenValue("test").header("alg", "none")
                .subject("learner").build();
        assertTrue(converter.convert(withoutRealm).isEmpty());
        assertTrue(converter.convert(jwt("invalid")).isEmpty());
        assertTrue(converter.convert(jwt(Map.of())).isEmpty());
        assertTrue(converter.convert(jwt(Map.of("roles", "ACCOUNTS"))).isEmpty());
    }

    @Test
    void ignoresNonStringAndBlankRoles() {
        var authorities = converter.convert(jwt(Map.of("roles", List.of("ACCOUNTS", 42, " "))));
        assertEquals(List.of("ROLE_ACCOUNTS"),
                authorities.stream().map(GrantedAuthority::getAuthority).toList());
    }

    private Jwt jwt(Object realmAccess) {
        return Jwt.withTokenValue("test").header("alg", "none")
                .claim("realm_access", realmAccess).build();
    }
}
