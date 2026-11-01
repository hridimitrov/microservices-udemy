package com.dimihris.gatewayserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigurationTests {

    @Test
    void usesSpringHttpMethodForPermitAllGetRule() throws IOException {
        String securityConfig = Files.readString(Path.of(
                "src/main/java/com/dimihris/gatewayserver/config/SecurityConfig.java"));
        String applicationProperties = Files.readString(Path.of(
                "src/main/resources/application.properties"));

        assertTrue(securityConfig.contains("import org.springframework.http.HttpMethod;"));
        assertFalse(securityConfig.contains("import jakarta.ws.rs.HttpMethod;"));
        assertFalse(applicationProperties.endsWith("certs\"\n"));
    }
}
