package com.irrigation.energy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EnergyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("✓ Energy Service Started Successfully!");
        System.out.println("✓ Port: 8081");
        System.out.println("✓ Managing pumps and energy consumption");
        System.out.println("===========================================\n");
    }
}
