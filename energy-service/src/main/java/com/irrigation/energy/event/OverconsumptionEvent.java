package com.irrigation.energy.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event publié par le microservice Énergie lorsqu'une surconsommation est détectée.
 * Consommé par le microservice Eau pour réagir aux événements de surconsommation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverconsumptionEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID de la pompe ayant surconsommé
     */
    private Long pompeId;
    
    /**
     * Énergie utilisée en kWh
     */
    private Double energieUtilisee;
    
    /**
     * Seuil de consommation dépassé en kWh
     */
    private Double seuil;
    
    /**
     * Date et heure de la mesure
     */
    private LocalDateTime dateMesure;
    
    /**
     * Message d'alerte
     */
    private String message;
}
