package com.irrigation.water.service;

import com.irrigation.water.client.EnergyServiceClient;
import com.irrigation.water.dto.DebitMesureDTO;
import com.irrigation.water.entity.DebitMesure;
import com.irrigation.water.repository.DebitMesureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DebitMesureService {

    private final DebitMesureRepository debitMesureRepository;
    private final EnergyServiceClient energyServiceClient;

    private static final Double PUMP_POWER_KW = 50.0; // Average pump power consumption

    public List<DebitMesureDTO> getAllDebits() {
        return debitMesureRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DebitMesureDTO getDebitById(Long id) {
        DebitMesure debit = debitMesureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Debit not found with id: " + id));
        return convertToDTO(debit);
    }

    public List<DebitMesureDTO> getDebitsByPompe(Long pompeId) {
        return debitMesureRepository.findByPompeId(pompeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DebitMesureDTO> getDebitsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        return debitMesureRepository.findByDateMesureBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DebitMesureDTO createDebit(DebitMesureDTO dto) {
        // ⚡ SYNCHRONOUS COMMUNICATION 1: Check if pompe has excessive consumption
        log.info("⚡ Checking pompe #{} consumption status...", dto.getPompeId());
        
        boolean pompeRestricted = energyServiceClient.isPompeRestricted(dto.getPompeId());
        
        if (pompeRestricted) {
            log.error("❌ Cannot start pump #{}: Pump restricted due to high energy consumption", dto.getPompeId());
            throw new RuntimeException("⚠️ Pump restricted due to high energy consumption (>= 150 kWh)");
        }
        
        // ⚡ SYNCHRONOUS COMMUNICATION 2: Check general energy availability
        log.info("🔌 Checking general energy availability before starting pump #{}", dto.getPompeId());
        
        boolean energyAvailable = energyServiceClient.checkEnergyAvailability(PUMP_POWER_KW);
        
        if (!energyAvailable) {
            log.error("❌ Cannot start pump #{}: Insufficient energy capacity", dto.getPompeId());
            throw new RuntimeException("⚠️ Insufficient energy to start pump. Current load too high.");
        }
        
        log.info("✅ All checks passed! Starting pump #{}", dto.getPompeId());
        
        DebitMesure debit = convertToEntity(dto);
        DebitMesure saved = debitMesureRepository.save(debit);
        return convertToDTO(saved);
    }

    public DebitMesureDTO updateDebit(Long id, DebitMesureDTO dto) {
        DebitMesure existing = debitMesureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Debit not found with id: " + id));
        
        existing.setDebit(dto.getDebit());
        existing.setUnite(dto.getUnite());
        
        DebitMesure updated = debitMesureRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deleteDebit(Long id) {
        if (!debitMesureRepository.existsById(id)) {
            throw new RuntimeException("Debit not found with id: " + id);
        }
        debitMesureRepository.deleteById(id);
    }

    public Double getDebitMoyenByPompe(Long pompeId) {
        Double moyen = debitMesureRepository.getDebitMoyenByPompe(pompeId);
        return moyen != null ? moyen : 0.0;
    }

    public Double getTotalDebitPeriode(LocalDateTime debut, LocalDateTime fin) {
        Double total = debitMesureRepository.getTotalDebitPeriode(debut, fin);
        return total != null ? total : 0.0;
    }

    private DebitMesureDTO convertToDTO(DebitMesure entity) {
        DebitMesureDTO dto = new DebitMesureDTO();
        dto.setId(entity.getId());
        dto.setPompeId(entity.getPompeId());
        dto.setDebit(entity.getDebit());
        dto.setDateMesure(entity.getDateMesure());
        dto.setUnite(entity.getUnite());
        return dto;
    }

    private DebitMesure convertToEntity(DebitMesureDTO dto) {
        DebitMesure entity = new DebitMesure();
        entity.setPompeId(dto.getPompeId());
        entity.setDebit(dto.getDebit());
        entity.setUnite(dto.getUnite());
        return entity;
    }
}
