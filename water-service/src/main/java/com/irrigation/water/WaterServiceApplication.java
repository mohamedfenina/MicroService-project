package com.irrigation.water;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WaterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("✓ Water Service Started Successfully!");
        System.out.println("✓ Port: 8082");
        System.out.println("✓ Managing reservoirs and water flow");
        System.out.println("===========================================\n");
    }
}
