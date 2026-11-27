package com.platform.common.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeonDatabaseEnvironmentPostProcessorTest {

    @Test
    void convertsNeonPooledUriToPgBouncerSafeJdbcUrl() {
        String uri = "postgresql://neondb_owner:secret123@ep-cool-host-pooler.us-east-2.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
        var parts = NeonDatabaseEnvironmentPostProcessor.parsePostgresUri(uri, true);

        assertEquals(
                "jdbc:postgresql://ep-cool-host-pooler.us-east-2.aws.neon.tech/neondb?sslmode=require&prepareThreshold=0",
                parts.jdbcUrl()
        );
        assertEquals("neondb_owner", parts.username());
        assertEquals("secret123", parts.password());
    }

    @Test
    void convertsNeonUnpooledUriForDirectFlywayMigrations() {
        String uri = "postgresql://neondb_owner:secret123@ep-cool-host.us-east-2.aws.neon.tech/neondb?sslmode=require";
        var parts = NeonDatabaseEnvironmentPostProcessor.parsePostgresUri(uri, false);

        assertEquals(
                "jdbc:postgresql://ep-cool-host.us-east-2.aws.neon.tech/neondb?sslmode=require",
                parts.jdbcUrl()
        );
        assertFalse(parts.jdbcUrl().contains("prepareThreshold"));
    }
}

