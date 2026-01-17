package com.irrigation.water.service;

import com.irrigation.water.dto.ReservoirDTO;
import com.irrigation.water.entity.Reservoir;
import com.irrigation.water.repository.ReservoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservoirService {

    private final ReservoirRepository reservoirRepository;

    public List<ReservoirDTO> getAllReservoirs() {
        return reservoirRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservoirDTO getReservoirById(Long id) {
        Reservoir reservoir = reservoirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with id: " + id));
        return convertToDTO(reservoir);
    }

    public ReservoirDTO getReservoirByNom(String nom) {
        Reservoir reservoir = reservoirRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with nom: " + nom));
        return convertToDTO(reservoir);
    }

    public List<ReservoirDTO> getReservoirsByLocalisation(String localisation) {
        return reservoirRepository.findByLocalisation(localisation).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservoirDTO createReservoir(ReservoirDTO dto) {
        Reservoir reservoir = convertToEntity(dto);
        Reservoir saved = reservoirRepository.save(reservoir);
        return convertToDTO(saved);
    }

    public ReservoirDTO updateReservoir(Long id, ReservoirDTO dto) {
        Reservoir existing = reservoirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with id: " + id));
        
        existing.setNom(dto.getNom());
        existing.setCapaciteTotale(dto.getCapaciteTotale());
        existing.setVolumeActuel(dto.getVolumeActuel());
        existing.setLocalisation(dto.getLocalisation());
        
        Reservoir updated = reservoirRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deleteReservoir(Long id) {
        if (!reservoirRepository.existsById(id)) {
            throw new RuntimeException("Reservoir not found with id: " + id);
        }
        reservoirRepository.deleteById(id);
    }

    public Double getTotalVolumeDisponible() {
        Double total = reservoirRepository.getTotalVolumeDisponible();
        return total != null ? total : 0.0;
    }

    public boolean isWaterAvailable(Double requiredVolume) {
        Double totalDisponible = getTotalVolumeDisponible();
        return totalDisponible >= requiredVolume;
    }

    private ReservoirDTO convertToDTO(Reservoir entity) {
        ReservoirDTO dto = new ReservoirDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setCapaciteTotale(entity.getCapaciteTotale());
        dto.setVolumeActuel(entity.getVolumeActuel());
        dto.setLocalisation(entity.getLocalisation());
        return dto;
    }

    private Reservoir convertToEntity(ReservoirDTO dto) {
        Reservoir entity = new Reservoir();
        entity.setNom(dto.getNom());
        entity.setCapaciteTotale(dto.getCapaciteTotale());
        entity.setVolumeActuel(dto.getVolumeActuel());
        entity.setLocalisation(dto.getLocalisation());
        return entity;
    }
}
