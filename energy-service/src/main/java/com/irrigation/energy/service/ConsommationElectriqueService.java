package com.irrigation.energy.service;

import com.irrigation.energy.dto.ConsommationElectriqueDTO;
import com.irrigation.energy.entity.ConsommationElectrique;
import com.irrigation.energy.event.OverconsumptionEvent;
import com.irrigation.energy.publisher.OverconsumptionPublisher;
import com.irrigation.energy.repository.ConsommationElectriqueRepository;
import com.irrigation.energy.repository.PompeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsommationElectriqueService {

    private final ConsommationElectriqueRepository consommationRepository;
    private final PompeRepository pompeRepository;
    private final OverconsumptionPublisher overconsumptionPublisher;
    
    // Seuil de surconsommation en kWh
    private static final Double SEUIL_SURCONSOMMATION = 100.0;

    public List<ConsommationElectriqueDTO> getAllConsommations() {
        return consommationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConsommationElectriqueDTO getConsommationById(Long id) {
        ConsommationElectrique consommation = consommationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consommation not found with id: " + id));
        return convertToDTO(consommation);
    }

    public List<ConsommationElectriqueDTO> getConsommationsByPompe(Long pompeId) {
        if (!pompeRepository.existsById(pompeId)) {
            throw new RuntimeException("Pompe not found with id: " + pompeId);
        }
        return consommationRepository.findByPompeId(pompeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ConsommationElectriqueDTO> getConsommationsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return consommationRepository.findByDateMesureBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConsommationElectriqueDTO createConsommation(ConsommationElectriqueDTO dto) {
        if (!pompeRepository.existsById(dto.getPompeId())) {
            throw new RuntimeException("Pompe not found with id: " + dto.getPompeId());
        }
        ConsommationElectrique consommation = convertToEntity(dto);
        ConsommationElectrique saved = consommationRepository.save(consommation);
        
        // Détection de surconsommation et publication d'événement
        detectAndPublishOverconsumption(saved);
        
        return convertToDTO(saved);
    }

    public ConsommationElectriqueDTO updateConsommation(Long id, ConsommationElectriqueDTO dto) {
        ConsommationElectrique existing = consommationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consommation not found with id: " + id));
        
        existing.setEnergieUtilisee(dto.getEnergieUtilisee());
        existing.setDuree(dto.getDuree());
        
        ConsommationElectrique updated = consommationRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deleteConsommation(Long id) {
        if (!consommationRepository.existsById(id)) {
            throw new RuntimeException("Consommation not found with id: " + id);
        }
        consommationRepository.deleteById(id);
    }

    public Double getTotalEnergieByPompe(Long pompeId) {
        if (!pompeRepository.existsById(pompeId)) {
            throw new RuntimeException("Pompe not found with id: " + pompeId);
        }
        Double total = consommationRepository.getTotalEnergieByPompe(pompeId);
        return total != null ? total : 0.0;
    }

    public Double getTotalEnergiePeriode(LocalDateTime debut, LocalDateTime fin) {
        Double total = consommationRepository.getTotalEnergiePeriode(debut, fin);
        return total != null ? total : 0.0;
    }

    private ConsommationElectriqueDTO convertToDTO(ConsommationElectrique entity) {
        ConsommationElectriqueDTO dto = new ConsommationElectriqueDTO();
        dto.setId(entity.getId());
        dto.setPompeId(entity.getPompeId());
        dto.setEnergieUtilisee(entity.getEnergieUtilisee());
        dto.setDuree(entity.getDuree());
        dto.setDateMesure(entity.getDateMesure());
        return dto;
    }

    private ConsommationElectrique convertToEntity(ConsommationElectriqueDTO dto) {
        ConsommationElectrique entity = new ConsommationElectrique();
        entity.setPompeId(dto.getPompeId());
        entity.setEnergieUtilisee(dto.getEnergieUtilisee());
        entity.setDuree(dto.getDuree());
        return entity;
    }
    
    /**
     * Détecte la surconsommation et publie un événement si le seuil est dépassé.
     * 
     * @param consommation La consommation électrique enregistrée
     */
    private void detectAndPublishOverconsumption(ConsommationElectrique consommation) {
        if (consommation.getEnergieUtilisee() > SEUIL_SURCONSOMMATION) {
            OverconsumptionEvent event = new OverconsumptionEvent(
                consommation.getPompeId(),
                consommation.getEnergieUtilisee(),
                SEUIL_SURCONSOMMATION,
                consommation.getDateMesure(),
                String.format("⚠️ ALERTE: Pompe #%d a consommé %.2f kWh (seuil: %.2f kWh)", 
                             consommation.getPompeId(), 
                             consommation.getEnergieUtilisee(), 
                             SEUIL_SURCONSOMMATION)
            );
            
            overconsumptionPublisher.publishOverconsumption(event);
        }
    }
    
    /**
     * Checks if a pompe has excessive energy consumption.
     * Used for synchronous communication from Water Service.
     * 
     * @param pompeId The ID of the pompe to check
     * @return true if pompe has excessive consumption (>= 150 kWh), false otherwise
     */
    public boolean hasPompeExcessiveConsumption(Long pompeId) {
        List<ConsommationElectrique> consommations = consommationRepository.findByPompeId(pompeId);
        
        boolean hasExcessive = consommations.stream()
            .anyMatch(c -> c.getEnergieUtilisee() >= 150.0);
        
        if (hasExcessive) {
            System.out.println(String.format("⚡ SYNC CHECK: Pompe #%d has EXCESSIVE consumption (>= 150 kWh)", pompeId));
        } else {
            System.out.println(String.format("⚡ SYNC CHECK: Pompe #%d consumption is NORMAL (< 150 kWh)", pompeId));
        }
        
        return hasExcessive;
    }
}
