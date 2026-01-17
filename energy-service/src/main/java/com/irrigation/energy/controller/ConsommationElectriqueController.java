package com.irrigation.energy.controller;

import com.irrigation.energy.dto.ConsommationElectriqueDTO;
import com.irrigation.energy.service.ConsommationElectriqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consommations")
@RequiredArgsConstructor
public class ConsommationElectriqueController {

    private final ConsommationElectriqueService consommationService;

    @GetMapping
    public ResponseEntity<List<ConsommationElectriqueDTO>> getAllConsommations() {
        return ResponseEntity.ok(consommationService.getAllConsommations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsommationElectriqueDTO> getConsommationById(@PathVariable Long id) {
        return ResponseEntity.ok(consommationService.getConsommationById(id));
    }

    @GetMapping("/pompe/{pompeId}")
    public ResponseEntity<List<ConsommationElectriqueDTO>> getConsommationsByPompe(@PathVariable Long pompeId) {
        return ResponseEntity.ok(consommationService.getConsommationsByPompe(pompeId));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<ConsommationElectriqueDTO>> getConsommationsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(consommationService.getConsommationsByPeriode(debut, fin));
    }

    @PostMapping
    public ResponseEntity<ConsommationElectriqueDTO> createConsommation(@Valid @RequestBody ConsommationElectriqueDTO dto) {
        ConsommationElectriqueDTO created = consommationService.createConsommation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsommationElectriqueDTO> updateConsommation(
            @PathVariable Long id,
            @Valid @RequestBody ConsommationElectriqueDTO dto) {
        return ResponseEntity.ok(consommationService.updateConsommation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsommation(@PathVariable Long id) {
        consommationService.deleteConsommation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total/pompe/{pompeId}")
    public ResponseEntity<Map<String, Object>> getTotalEnergieByPompe(@PathVariable Long pompeId) {
        Double total = consommationService.getTotalEnergieByPompe(pompeId);
        Map<String, Object> response = new HashMap<>();
        response.put("pompeId", pompeId);
        response.put("totalEnergie", total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total/periode")
    public ResponseEntity<Map<String, Object>> getTotalEnergiePeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Double total = consommationService.getTotalEnergiePeriode(debut, fin);
        Map<String, Object> response = new HashMap<>();
        response.put("debut", debut);
        response.put("fin", fin);
        response.put("totalEnergie", total);
        return ResponseEntity.ok(response);
    }
    
    /**
     * SYNCHRONOUS ENDPOINT for Water Service
     * Checks if a pompe has excessive energy consumption (>= 150 kWh)
     * 
     * @param pompeId The ID of the pompe to check
     * @return Response with restriction status
     */
    @GetMapping("/check/pompe/{pompeId}")
    public ResponseEntity<Map<String, Object>> checkPompeStatus(@PathVariable Long pompeId) {
        boolean hasExcessive = consommationService.hasPompeExcessiveConsumption(pompeId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("pompeId", pompeId);
        response.put("restricted", hasExcessive);
        response.put("message", hasExcessive 
            ? "⚠️ Pump restricted due to high energy consumption" 
            : "✅ Pump available for operation");
        
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
