package com.dimihris.accountsservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DockerComposeConfigurationTests {

    @Test
    void includesInfrastructureAndPreservesExistingKeycloakVolume() throws IOException {
        String compose = Files.readString(Path.of("docker-compose.yaml")).replace("\r\n", "\n");

        assertTrue(compose.contains("  redis:"));
        assertTrue(compose.contains("image: redis:"));
        assertTrue(compose.contains("6379:6379"));
        assertTrue(compose.contains("  kafka:"));
        assertTrue(compose.contains("SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS: kafka:29092"));
        assertTrue(compose.contains("SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:29092"));
        assertTrue(compose.contains("/opt/kafka/bin/kafka-broker-api-versions.sh"));
        assertFalse(compose.toLowerCase().contains("rabbitmq"));
        assertFalse(compose.contains(":s13"));
        assertTrue(compose.contains("keycloak-data:\n    external: true\n    name: accounts-service_keycloak-data"));
    }
}
