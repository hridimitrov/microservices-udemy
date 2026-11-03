package com.dimihris.accountsservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DockerComposeConfigurationTests {

    @Test
    void includesInfrastructureAndPreservesExistingKeycloakVolume() throws IOException {
        String compose = Files.readString(Path.of("docker-compose.yaml")).replace("\r\n", "\n");

        assertTrue(compose.contains("  redis:"));
        assertTrue(compose.contains("image: redis:"));
        assertTrue(compose.contains("6379:6379"));
        assertTrue(compose.contains("  rabbitmq:"));
        assertTrue(compose.contains("SPRING_RABBITMQ_HOST: rabbitmq"));
        assertTrue(compose.contains("keycloak-data:\n    external: true\n    name: accounts-service_keycloak-data"));
    }
}
