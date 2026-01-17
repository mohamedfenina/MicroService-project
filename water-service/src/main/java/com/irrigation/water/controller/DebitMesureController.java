package com.irrigation.water.controller;

import com.irrigation.water.dto.DebitMesureDTO;
import com.irrigation.water.service.DebitMesureService;
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
@RequestMapping("/debits")
@RequiredArgsConstructor
public class DebitMesureController {

    private final DebitMesureService debitMesureService;

    @GetMapping
    public ResponseEntity<List<DebitMesureDTO>> getAllDebits() {
        return ResponseEntity.ok(debitMesureService.getAllDebits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebitMesureDTO> getDebitById(@PathVariable Long id) {
        return ResponseEntity.ok(debitMesureService.getDebitById(id));
    }

    @GetMapping("/pompe/{pompeId}")
    public ResponseEntity<List<DebitMesureDTO>> getDebitsByPompe(@PathVariable Long pompeId) {
        return ResponseEntity.ok(debitMesureService.getDebitsByPompe(pompeId));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<DebitMesureDTO>> getDebitsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(debitMesureService.getDebitsByPeriode(debut, fin));
    }

    @PostMapping
    public ResponseEntity<DebitMesureDTO> createDebit(@Valid @RequestBody DebitMesureDTO dto) {
        DebitMesureDTO created = debitMesureService.createDebit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DebitMesureDTO> updateDebit(@PathVariable Long id, @Valid @RequestBody DebitMesureDTO dto) {
        return ResponseEntity.ok(debitMesureService.updateDebit(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebit(@PathVariable Long id) {
        debitMesureService.deleteDebit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/moyen/pompe/{pompeId}")
    public ResponseEntity<Map<String, Object>> getDebitMoyenByPompe(@PathVariable Long pompeId) {
        Double moyen = debitMesureService.getDebitMoyenByPompe(pompeId);
        Map<String, Object> response = new HashMap<>();
        response.put("pompeId", pompeId);
        response.put("debitMoyen", moyen);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total/periode")
    public ResponseEntity<Map<String, Object>> getTotalDebitPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Double total = debitMesureService.getTotalDebitPeriode(debut, fin);
        Map<String, Object> response = new HashMap<>();
        response.put("debut", debut);
        response.put("fin", fin);
        response.put("totalDebit", total);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
