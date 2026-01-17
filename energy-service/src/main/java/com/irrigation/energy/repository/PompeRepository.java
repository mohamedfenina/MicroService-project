package com.irrigation.energy.repository;

import com.irrigation.energy.entity.Pompe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PompeRepository extends JpaRepository<Pompe, Long> {
    
    Optional<Pompe> findByReference(String reference);
    
    List<Pompe> findByStatut(Pompe.StatutPompe statut);
    
    boolean existsByReference(String reference);
    
    @Query("SELECT SUM(p.puissance) FROM Pompe p WHERE p.statut = 'ACTIVE'")
    Double getTotalPuissanceActive();
}
