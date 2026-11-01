package com.dimihris.accountsservice;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DockerComposeConfigurationTests {

    @Test
    void excludesRedisFromTheBankingStack() throws IOException {
        String compose = Files.readString(Path.of("docker-compose.yaml"));

        assertFalse(compose.contains("  redis:"));
        assertFalse(compose.contains("image: redis:"));
        assertFalse(compose.contains("6379:6379"));
        assertFalse(compose.contains("container_name: redis"));
        assertFalse(compose.contains("SPRING_DATA_REDIS"));
    }
}
