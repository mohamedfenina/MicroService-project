package com.irrigation.water.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitMesureDTO {
    
    private Long id;
    
    @NotNull(message = "Pompe ID is required")
    private Long pompeId;
    
    @NotNull(message = "Debit is required")
    @Positive(message = "Debit must be positive")
    private Double debit;
    
    private LocalDateTime dateMesure;
    
    @NotBlank(message = "Unite is required")
    private String unite;
}
