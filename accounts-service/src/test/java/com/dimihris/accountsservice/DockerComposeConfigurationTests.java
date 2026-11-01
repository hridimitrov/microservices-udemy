package com.dimihris.accountsservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DockerComposeConfigurationTests {

    @Test
    void includesRedisOnTheBankingNetwork() throws IOException {
        String compose = Files.readString(Path.of("docker-compose.yaml"));

        assertTrue(compose.contains("  redis:"));
        assertTrue(compose.contains("image: redis:"));
        assertTrue(compose.contains("\"6379:6379\""));
        assertTrue(compose.contains("container_name: redis"));
        assertTrue(compose.contains("- \"banking-network\""));
    }
}
