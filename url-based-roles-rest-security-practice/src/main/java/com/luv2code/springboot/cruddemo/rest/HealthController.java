package com.luv2code.springboot.cruddemo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * HealthController provides health check and basic information endpoints
 * for the Employee Management API. These endpoints are publicly accessible
 * and do not require authentication.
 */
@Tag(name = "Health & Status", description = "API health monitoring and status endpoints")
@RestController
public class HealthController {

    /**
     * Health check endpoint to verify API service status.
     * This endpoint is typically used by monitoring systems, load balancers,
     * and container orchestration platforms to determine service availability.
     *
     * @return Map containing service status information with keys:
     *         - "status": Service status (UP/DOWN)
     *         - "service": Service identifier
     */
    @Operation(
        summary = "Health check endpoint",
        description = "Returns the current health status of the API service. Used for monitoring and availability checks.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Service is healthy and running"),
            @ApiResponse(responseCode = "503", description = "Service is unavailable")
        }
    )
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "Employee Management API");
    }

    /**
     * Root endpoint providing basic API information.
     * Serves as the entry point for the API and provides a welcome message
     * with service identification.
     *
     * @return Map containing welcome message and service information
     */
    @Operation(
        summary = "API root endpoint",
        description = "Returns a welcome message and basic information about the API service.",
        responses = {
            @ApiResponse(responseCode = "200", description = "API is running successfully")
        }
    )
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Employee Management API is running");
    }
}