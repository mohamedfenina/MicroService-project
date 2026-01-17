package com.irrigation.water.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * REST Template Configuration for Synchronous Communication
 * Enables Water Service to call Energy Service
 */
@Configuration
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced  // Eureka service discovery
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
