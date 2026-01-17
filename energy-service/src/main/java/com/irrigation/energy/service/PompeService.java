package com.irrigation.energy.service;

import com.irrigation.energy.dto.PompeDTO;
import com.irrigation.energy.entity.Pompe;
import com.irrigation.energy.repository.PompeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PompeService {

    private final PompeRepository pompeRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Double MAX_PUISSANCE = 1000.0;

    public List<PompeDTO> getAllPompes() {
        return pompeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PompeDTO getPompeById(Long id) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));
        return convertToDTO(pompe);
    }

    public PompeDTO getPompeByReference(String reference) {
        Pompe pompe = pompeRepository.findByReference(reference)
                .orElseThrow(() -> new RuntimeException("Pompe not found with reference: " + reference));
        return convertToDTO(pompe);
    }

    public List<PompeDTO> getPompesByStatut(Pompe.StatutPompe statut) {
        return pompeRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PompeDTO createPompe(PompeDTO dto) {
        if (pompeRepository.existsByReference(dto.getReference())) {
            throw new RuntimeException("Pompe with reference " + dto.getReference() + " already exists");
        }
        Pompe pompe = convertToEntity(dto);
        Pompe saved = pompeRepository.save(pompe);
        return convertToDTO(saved);
    }

    public PompeDTO updatePompe(Long id, PompeDTO dto) {
        Pompe existing = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));
        
        existing.setPuissance(dto.getPuissance());
        existing.setStatut(dto.getStatut());
        
        Pompe updated = pompeRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deletePompe(Long id) {
        if (!pompeRepository.existsById(id)) {
            throw new RuntimeException("Pompe not found with id: " + id);
        }
        pompeRepository.deleteById(id);
    }

    public boolean isEnergyAvailable(Double requiredPuissance) {
        Double totalActive = pompeRepository.getTotalPuissanceActive();
        if (totalActive == null) totalActive = 0.0;
        return (totalActive + requiredPuissance) <= MAX_PUISSANCE;
    }

    public void updateEnergyStatus(Long id, String status) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));
        pompe.setEnergyStatus(status);
        pompeRepository.save(pompe);
    }

    private PompeDTO convertToDTO(Pompe entity) {
        PompeDTO dto = new PompeDTO();
        dto.setId(entity.getId());
        dto.setReference(entity.getReference());
        dto.setPuissance(entity.getPuissance());
        dto.setStatut(entity.getStatut());
        dto.setDateMiseEnService(entity.getDateMiseEnService() != null ? 
                entity.getDateMiseEnService().format(DATE_FORMATTER) : null);
        dto.setEnergyStatus(entity.getEnergyStatus());
        return dto;
    }

    private Pompe convertToEntity(PompeDTO dto) {
        Pompe entity = new Pompe();
        entity.setReference(dto.getReference());
        entity.setPuissance(dto.getPuissance());
        entity.setStatut(dto.getStatut());
        return entity;
    }
}
