package com.irrigation.energy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationElectriqueDTO {
    
    private Long id;
    
    @NotNull(message = "Pompe ID is required")
    private Long pompeId;
    
    @NotNull(message = "Energie utilisee is required")
    @Positive(message = "Energie must be positive")
    private Double energieUtilisee;
    
    @NotNull(message = "Duree is required")
    @Positive(message = "Duree must be positive")
    private Double duree;
    
    private LocalDateTime dateMesure;
}
