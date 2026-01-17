package com.irrigation.water.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Synchronous REST Client to Energy Service
 * Eau → Énergie: Check electrical availability before pump start
 */
@Slf4j
@Component
public class EnergyServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String ENERGY_SERVICE_NAME = "ENERGY-SERVICE";

    /**
     * SYNCHRONOUS CALL: Check if energy is available before starting pump
     * 
     * @param requiredPower Power required in kW
     * @return true if energy available, false otherwise
     */
    public boolean checkEnergyAvailability(Double requiredPower) {
        String url = String.format("http://%s/pompes/energy/check?requiredPuissance=%s", 
                                   ENERGY_SERVICE_NAME, requiredPower);
        
        try {
            log.info("⚡ SYNC CALL → Energy Service: Checking availability for {}kW", requiredPower);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            boolean available = (Boolean) response.get("available");
            String message = (String) response.get("message");
            
            log.info("⚡ SYNC RESPONSE ← Energy Service: {} - {}", available, message);
            
            return available;
            
        } catch (Exception e) {
            log.error("❌ Failed to check energy availability: {}", e.getMessage());
            throw new RuntimeException("Energy Service unreachable", e);
        }
    }
    
    /**
     * SYNCHRONOUS CALL: Check if pompe has excessive energy consumption
     * 
     * @param pompeId ID of the pompe to check
     * @return true if pompe is restricted (has excessive consumption), false otherwise
     */
    public boolean isPompeRestricted(Long pompeId) {
        String url = String.format("http://%s/consommations/check/pompe/%d", 
                                   ENERGY_SERVICE_NAME, pompeId);
        
        try {
            log.info("⚡ SYNC CALL → Energy Service: Checking pompe #{} consumption status", pompeId);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            boolean restricted = (Boolean) response.get("restricted");
            String message = (String) response.get("message");
            
            log.info("⚡ SYNC RESPONSE ← Energy Service: {} - {}", restricted, message);
            
            return restricted;
            
        } catch (Exception e) {
            log.error("❌ Failed to check pompe status: {}", e.getMessage());
            // Return false on error to avoid blocking operations
            return false;
        }
    }
}
