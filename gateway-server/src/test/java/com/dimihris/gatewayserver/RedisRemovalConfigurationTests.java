package com.dimihris.gatewayserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RedisRemovalConfigurationTests {

    @Test
    void gatewayHasNoRedisRateLimiterConfiguration() throws IOException {
        String application = Files.readString(Path.of("src/main/java/com/dimihris/gatewayserver/GatewayServerApplication.java"));
        String properties = Files.readString(Path.of("src/main/resources/application.properties"));
        String build = Files.readString(Path.of("build.gradle"));

        assertFalse(application.contains("RedisRateLimiter"));
        assertFalse(application.contains("requestRateLimiter"));
        assertFalse(properties.contains("spring.data.redis"));
        assertFalse(build.contains("spring-boot-starter-data-redis-reactive"));
    }
}
