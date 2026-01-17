package com.irrigation.water.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservoirDTO {
    
    private Long id;
    
    @NotBlank(message = "Nom is required")
    private String nom;
    
    @NotNull(message = "Capacite totale is required")
    @Positive(message = "Capacite must be positive")
    private Double capaciteTotale;
    
    @NotNull(message = "Volume actuel is required")
    private Double volumeActuel;
    
    @NotBlank(message = "Localisation is required")
    private String localisation;
}
