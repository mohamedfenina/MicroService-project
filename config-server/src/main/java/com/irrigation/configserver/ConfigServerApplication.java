package com.irrigation.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server Application
 * 
 * Centralized configuration management for all microservices.
 * Serves configuration from Git repository.
 * 
 * @author Irrigation System Team
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("===========================================");
        System.out.println("✅ Config Server Started Successfully!");
        System.out.println("📍 Port: 8888");
        System.out.println("🔗 Access: http://localhost:8888");
        System.out.println("===========================================");
    }
}
