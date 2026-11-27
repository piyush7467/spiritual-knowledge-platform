package com.platform.common.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wires Neon's pulled libpq connection strings (DATABASE_URL pooled and
 * DATABASE_URL_UNPOOLED direct) into Spring DataSource and Flyway properties.
 * Follows Neon best practices: pooled connection with prepareThreshold=0 for
 * runtime queries, and direct/unpooled connection for Flyway migrations.
 */
public class NeonDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final List<Path> ENV_CANDIDATES = List.of(
            Path.of(".env.local"),
            Path.of(".env"),
            Path.of("..", ".env.local"),
            Path.of("..", ".env"),
            Path.of("..", "..", ".env.local"),
            Path.of("..", "..", ".env")
    );

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String existingDbUrl = environment.getProperty("DB_URL", "");
        boolean hasExplicitJdbc = !existingDbUrl.isBlank() && !existingDbUrl.contains("HOST/DB_NAME");

        Map<String, String> fileVars = loadRootEnvVars();
        String pooledUrl = firstNonBlank(environment.getProperty("DATABASE_URL"), fileVars.get("DATABASE_URL"));
        String directUrl = firstNonBlank(environment.getProperty("DATABASE_URL_UNPOOLED"), fileVars.get("DATABASE_URL_UNPOOLED"));

        Map<String, Object> overrides = new HashMap<>();
        if (!hasExplicitJdbc && pooledUrl != null && !pooledUrl.isBlank()) {
            JdbcParts pooled = parsePostgresUri(pooledUrl, true);
            overrides.put("spring.datasource.url", pooled.jdbcUrl());
            if (!pooled.username().isBlank()) {
                overrides.put("spring.datasource.username", pooled.username());
            }
            if (!pooled.password().isBlank()) {
                overrides.put("spring.datasource.password", pooled.password());
            }
        } else if (hasExplicitJdbc && existingDbUrl.contains("-pooler") && !existingDbUrl.contains("prepareThreshold=")) {
            overrides.put("spring.datasource.url", appendPoolerOptionsIfNeeded(existingDbUrl, true));
        }

        // Neon best practice: always run Flyway migrations over the direct/unpooled connection when available
        String migrationUrl = (directUrl != null && !directUrl.isBlank())
                ? directUrl
                : (!hasExplicitJdbc ? pooledUrl : null);
        if (migrationUrl != null && !migrationUrl.isBlank()) {
            JdbcParts direct = parsePostgresUri(migrationUrl, false);
            overrides.put("spring.flyway.url", direct.jdbcUrl());
            if (!direct.username().isBlank()) {
                overrides.put("spring.flyway.user", direct.username());
            }
            if (!direct.password().isBlank()) {
                overrides.put("spring.flyway.password", direct.password());
            }
        }

        if (!overrides.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("neonPostgresEnv", overrides));
        }
    }

    static JdbcParts parsePostgresUri(String rawUri, boolean pooledRuntime) {
        String trimmed = rawUri.trim();
        if (trimmed.startsWith("jdbc:postgresql://")) {
            return new JdbcParts(appendPoolerOptionsIfNeeded(trimmed, pooledRuntime), "", "");
        }
        String normalized = trimmed.replaceFirst("^postgres(ql)?://", "http://");
        URI uri = URI.create(normalized);

        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath() != null ? uri.getPath() : "";
        String query = uri.getRawQuery();

        String username = "";
        String password = "";
        if (uri.getRawUserInfo() != null && !uri.getRawUserInfo().isBlank()) {
            String[] parts = uri.getRawUserInfo().split(":", 2);
            username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            if (parts.length > 1) {
                password = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            }
        }

        StringBuilder jdbc = new StringBuilder("jdbc:postgresql://").append(host);
        if (port > 0) {
            jdbc.append(':').append(port);
        }
        jdbc.append(path);
        if (query != null && !query.isBlank()) {
            jdbc.append('?').append(sanitizeQuery(query));
        }
        return new JdbcParts(appendPoolerOptionsIfNeeded(jdbc.toString(), pooledRuntime), username, password);
    }

    private static String sanitizeQuery(String query) {
        // Strip libpq-only parameters unsupported by pgjdbc (e.g. channel_binding)
        List<String> kept = java.util.Arrays.stream(query.split("&"))
                .filter(p -> !p.isBlank() && !p.startsWith("channel_binding="))
                .toList();
        return String.join("&", kept);
    }

    private static String appendPoolerOptionsIfNeeded(String jdbcUrl, boolean pooledRuntime) {
        if (!pooledRuntime || !jdbcUrl.contains("-pooler") || jdbcUrl.contains("prepareThreshold=")) {
            return jdbcUrl;
        }
        return jdbcUrl + (jdbcUrl.contains("?") ? "&" : "?") + "prepareThreshold=0";
    }

    private static Map<String, String> loadRootEnvVars() {
        Map<String, String> vars = new HashMap<>();
        for (Path candidate : ENV_CANDIDATES) {
            if (!Files.isRegularFile(candidate)) {
                continue;
            }
            try {
                for (String line : Files.readAllLines(candidate, StandardCharsets.UTF_8)) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue;
                    }
                    int eq = trimmed.indexOf('=');
                    if (eq <= 0) {
                        continue;
                    }
                    String key = trimmed.substring(0, eq).trim();
                    String val = unquote(trimmed.substring(eq + 1).trim());
                    vars.putIfAbsent(key, val);
                }
            } catch (IOException ignored) {
                // Skip unreadable candidate
            }
        }
        return vars;
    }

    private static String unquote(String val) {
        if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
            return val.substring(1, val.length() - 1);
        }
        return val;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    record JdbcParts(String jdbcUrl, String username, String password) {}
}

