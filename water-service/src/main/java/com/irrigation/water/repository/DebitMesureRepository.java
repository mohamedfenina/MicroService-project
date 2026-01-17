package com.irrigation.water.repository;

import com.irrigation.water.entity.DebitMesure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DebitMesureRepository extends JpaRepository<DebitMesure, Long> {
    
    List<DebitMesure> findByPompeId(Long pompeId);
    
    List<DebitMesure> findByDateMesureBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT AVG(d.debit) FROM DebitMesure d WHERE d.pompeId = :pompeId")
    Double getDebitMoyenByPompe(Long pompeId);
    
    @Query("SELECT SUM(d.debit) FROM DebitMesure d WHERE d.dateMesure BETWEEN :debut AND :fin")
    Double getTotalDebitPeriode(LocalDateTime debut, LocalDateTime fin);
}
