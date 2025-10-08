package com.luv2code.springboot.cruddemo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "Employee Management API");
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Employee Management API is running");
    }
}

