package com.example.stock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Health check controller for monitoring application status.
 * Provides endpoints to check application health and system information.
 * 
 * @author Generated
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Health Check", description = "APIs for monitoring application health and status")
public class HealthController {

    private final Environment environment;
    private final DataSource dataSource;
    private final Optional<BuildProperties> buildProperties;

    /**
     * Basic health check endpoint.
     * Returns simple status information.
     */
    @GetMapping
    @Operation(summary = "Health check", description = "Returns basic application health status")
    @ApiResponse(responseCode = "200", description = "Application is healthy")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("application", environment.getProperty("spring.application.name"));
        health.put("profile", environment.getActiveProfiles());
        
        return ResponseEntity.ok(health);
    }

    /**
     * Detailed system information endpoint.
     * Returns comprehensive system and application information.
     */
    @GetMapping("/info")
    @Operation(summary = "System information", description = "Returns detailed system and application information")
    @ApiResponse(responseCode = "200", description = "System information retrieved successfully")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        
        // Application info
        info.put("application", Map.of(
            "name", environment.getProperty("spring.application.name", "Stock Management System"),
            "version", buildProperties.map(BuildProperties::getVersion).orElse("1.0.0"),
            "profiles", environment.getActiveProfiles(),
            "port", environment.getProperty("server.port", "8080")
        ));
        
        // System info
        info.put("system", Map.of(
            "java.version", System.getProperty("java.version"),
            "java.vendor", System.getProperty("java.vendor"),
            "os.name", System.getProperty("os.name"),
            "os.version", System.getProperty("os.version"),
            "timestamp", LocalDateTime.now()
        ));
        
        // Database connectivity
        try (Connection connection = dataSource.getConnection()) {
            info.put("database", Map.of(
                "status", "UP",
                "url", connection.getMetaData().getURL(),
                "driver", connection.getMetaData().getDriverName(),
                "version", connection.getMetaData().getDatabaseProductVersion()
            ));
        } catch (Exception e) {
            info.put("database", Map.of(
                "status", "DOWN",
                "error", e.getMessage()
            ));
        }
        
        return ResponseEntity.ok(info);
    }

    /**
     * Database connectivity check.
     * Tests database connection and returns status.
     */
    @GetMapping("/db")
    @Operation(summary = "Database health check", description = "Tests database connectivity and returns status")
    @ApiResponse(responseCode = "200", description = "Database connection test completed")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            dbHealth.put("status", "UP");
            dbHealth.put("database", connection.getMetaData().getDatabaseProductName());
            dbHealth.put("version", connection.getMetaData().getDatabaseProductVersion());
            dbHealth.put("url", connection.getMetaData().getURL());
            dbHealth.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(dbHealth);
        } catch (Exception e) {
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
            dbHealth.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(503).body(dbHealth);
        }
    }
}
