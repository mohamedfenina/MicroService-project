package com.irrigation.energy.publisher;

import com.irrigation.energy.config.RabbitMQConfig;
import com.irrigation.energy.event.OverconsumptionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la publication des événements de surconsommation vers RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OverconsumptionPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    /**
     * Publie un événement de surconsommation vers RabbitMQ.
     * 
     * @param event L'événement de surconsommation à publier
     */
    public void publishOverconsumption(OverconsumptionEvent event) {
        log.info("📤 Publishing overconsumption event: Pompe ID={}, Energy={} kWh, Threshold={} kWh", 
                 event.getPompeId(), event.getEnergieUtilisee(), event.getSeuil());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE, 
            RabbitMQConfig.ROUTING_KEY, 
            event
        );
        
        log.info("✅ Overconsumption event published successfully");
    }
}
