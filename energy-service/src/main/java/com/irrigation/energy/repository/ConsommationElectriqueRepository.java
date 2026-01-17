package com.irrigation.energy.repository;

import com.irrigation.energy.entity.ConsommationElectrique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsommationElectriqueRepository extends JpaRepository<ConsommationElectrique, Long> {
    
    List<ConsommationElectrique> findByPompeId(Long pompeId);
    
    List<ConsommationElectrique> findByDateMesureBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT SUM(c.energieUtilisee) FROM ConsommationElectrique c WHERE c.pompeId = :pompeId")
    Double getTotalEnergieByPompe(Long pompeId);
    
    @Query("SELECT SUM(c.energieUtilisee) FROM ConsommationElectrique c WHERE c.dateMesure BETWEEN :debut AND :fin")
    Double getTotalEnergiePeriode(LocalDateTime debut, LocalDateTime fin);
}
