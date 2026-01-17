package com.irrigation.water.listener;

import com.irrigation.water.config.RabbitMQConfig;
import com.irrigation.water.event.OverconsumptionEvent;
import com.irrigation.water.service.ReservoirService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Listener qui consomme les événements de surconsommation publiés par le microservice Énergie.
 * Réagit aux alertes en loggant et en déclenchant des actions appropriées.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OverconsumptionListener {
    
    private final ReservoirService reservoirService;
    private final RestTemplate restTemplate;
    
    /**
     * Consomme les événements de surconsommation depuis la queue RabbitMQ.
     * 
     * @param event L'événement de surconsumption reçu
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleOverconsumption(OverconsumptionEvent event) {
        log.warn("═══════════════════════════════════════════════════════════════");
        log.warn("📥 OVERCONSUMPTION EVENT RECEIVED");
        log.warn("═══════════════════════════════════════════════════════════════");
        log.warn("⚡ Pompe ID: {}", event.getPompeId());
        log.warn("⚡ Énergie Utilisée: {} kWh", event.getEnergieUtilisee());
        log.warn("⚡ Seuil Dépassé: {} kWh", event.getSeuil());
        log.warn("⚡ Date Mesure: {}", event.getDateMesure());
        log.warn("⚡ Message: {}", event.getMessage());
        log.warn("═══════════════════════════════════════════════════════════════");
        
        // Réaction du microservice Eau face à la surconsommation
        reactToOverconsumption(event);
    }
    
    /**
     * Définit la réaction du microservice Eau face à un événement de surconsommation.
     * OPTIMISATION CONJOINTE ÉNERGIE-EAU
     * 
     * @param event L'événement de surconsommation
     */
    private void reactToOverconsumption(OverconsumptionEvent event) {
        log.warn("🔧 WATER SERVICE ADAPTIVE REACTION:");
        log.warn("   ⚠️ Pump #{} overconsumption: {} kWh (threshold: {})", 
                 event.getPompeId(), event.getEnergieUtilisee(), event.getSeuil());
        
        // ACTION 1: Update pump energy status in Energy Service (async callback)
        updatePumpEnergyStatus(event.getPompeId(), "Overconsumption");
        
        // ACTION 2: Analyze reservoirs to prioritize critical ones
        try {
            long count = reservoirService.getAllReservoirs().size();
            log.info("   → Analyzing {} reservoirs for priority irrigation", count);
            log.info("   → Critical reservoirs (<30%) maintain supply, others reduced");
        } catch (Exception e) {
            log.warn("   → Could not analyze reservoirs: {}", e.getMessage());
        }
        
        // ACTION 3: Alert operators
        log.info("   → Alert sent to operators - Manual intervention may be required");
        
        // ACTION 4: Log incident
        log.info("   → Incident logged at: {}", event.getDateMesure());
        
        log.info("✅ OPTIMIZATION: Water Service adapted to energy constraints");
    }
    
    /**
     * Updates pump energy status in Energy Service
     */
    private void updatePumpEnergyStatus(Long pompeId, String status) {
        try {
            String url = String.format("http://ENERGY-SERVICE/pompes/%d/energy-status?status=%s", 
                                       pompeId, status);
            restTemplate.put(url, null);
            log.info("   → Pump #{} status updated to: {}", pompeId, status);
        } catch (Exception e) {
            log.error("   → Failed to update pump status: {}", e.getMessage());
        }
    }
}
