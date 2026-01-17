package com.irrigation.energy.dto;

import com.irrigation.energy.entity.Pompe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PompeDTO {
    
    private Long id;
    
    @NotBlank(message = "Reference is required")
    private String reference;
    
    @NotNull(message = "Puissance is required")
    @Positive(message = "Puissance must be positive")
    private Double puissance;
    
    @NotNull(message = "Statut is required")
    private Pompe.StatutPompe statut;
    
    private String dateMiseEnService;
    
    private String energyStatus;
}
