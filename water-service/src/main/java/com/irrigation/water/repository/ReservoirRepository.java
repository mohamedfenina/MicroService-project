package com.irrigation.water.repository;

import com.irrigation.water.entity.Reservoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservoirRepository extends JpaRepository<Reservoir, Long> {
    
    Optional<Reservoir> findByNom(String nom);
    
    List<Reservoir> findByLocalisation(String localisation);
    
    @Query("SELECT SUM(r.volumeActuel) FROM Reservoir r")
    Double getTotalVolumeDisponible();
}
