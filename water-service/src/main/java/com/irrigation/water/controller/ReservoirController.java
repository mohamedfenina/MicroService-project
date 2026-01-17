package com.irrigation.water.controller;

import com.irrigation.water.dto.ReservoirDTO;
import com.irrigation.water.service.ReservoirService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservoirs")
@RequiredArgsConstructor
public class ReservoirController {

    private final ReservoirService reservoirService;

    @GetMapping
    public ResponseEntity<List<ReservoirDTO>> getAllReservoirs() {
        return ResponseEntity.ok(reservoirService.getAllReservoirs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservoirDTO> getReservoirById(@PathVariable Long id) {
        return ResponseEntity.ok(reservoirService.getReservoirById(id));
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<ReservoirDTO> getReservoirByNom(@PathVariable String nom) {
        return ResponseEntity.ok(reservoirService.getReservoirByNom(nom));
    }

    @GetMapping("/localisation/{localisation}")
    public ResponseEntity<List<ReservoirDTO>> getReservoirsByLocalisation(@PathVariable String localisation) {
        return ResponseEntity.ok(reservoirService.getReservoirsByLocalisation(localisation));
    }

    @PostMapping
    public ResponseEntity<ReservoirDTO> createReservoir(@Valid @RequestBody ReservoirDTO dto) {
        ReservoirDTO created = reservoirService.createReservoir(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservoirDTO> updateReservoir(@PathVariable Long id, @Valid @RequestBody ReservoirDTO dto) {
        return ResponseEntity.ok(reservoirService.updateReservoir(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservoir(@PathVariable Long id) {
        reservoirService.deleteReservoir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/water/check")
    public ResponseEntity<Map<String, Object>> checkWaterAvailability(@RequestParam Double requiredVolume) {
        boolean available = reservoirService.isWaterAvailable(requiredVolume);
        Double totalDisponible = reservoirService.getTotalVolumeDisponible();
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("requiredVolume", requiredVolume);
        response.put("totalDisponible", totalDisponible);
        response.put("message", available ? "Water available" : "Insufficient water");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
