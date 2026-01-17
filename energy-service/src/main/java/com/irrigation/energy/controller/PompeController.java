package com.irrigation.energy.controller;

import com.irrigation.energy.dto.PompeDTO;
import com.irrigation.energy.entity.Pompe;
import com.irrigation.energy.service.PompeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pompes")
@RequiredArgsConstructor
public class PompeController {

    private final PompeService pompeService;

    @GetMapping
    public ResponseEntity<List<PompeDTO>> getAllPompes() {
        return ResponseEntity.ok(pompeService.getAllPompes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PompeDTO> getPompeById(@PathVariable Long id) {
        return ResponseEntity.ok(pompeService.getPompeById(id));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<PompeDTO> getPompeByReference(@PathVariable String reference) {
        return ResponseEntity.ok(pompeService.getPompeByReference(reference));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<PompeDTO>> getPompesByStatut(@PathVariable Pompe.StatutPompe statut) {
        return ResponseEntity.ok(pompeService.getPompesByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<PompeDTO> createPompe(@Valid @RequestBody PompeDTO dto) {
        PompeDTO created = pompeService.createPompe(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PompeDTO> updatePompe(@PathVariable Long id, @Valid @RequestBody PompeDTO dto) {
        return ResponseEntity.ok(pompeService.updatePompe(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePompe(@PathVariable Long id) {
        pompeService.deletePompe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/energy/check")
    public ResponseEntity<Map<String, Object>> checkEnergyAvailability(@RequestParam Double requiredPuissance) {
        boolean available = pompeService.isEnergyAvailable(requiredPuissance);
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("requiredPuissance", requiredPuissance);
        response.put("message", available ? "Energy available" : "Insufficient energy capacity");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/energy-status")
    public ResponseEntity<Map<String, String>> updateEnergyStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        pompeService.updateEnergyStatus(id, status);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Energy status updated to " + status);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
