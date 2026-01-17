package com.irrigation.energy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consommations_electriques")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationElectrique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "pompe_id")
    private Long pompeId;

    @Column(nullable = false, name = "energie_utilisee")
    private Double energieUtilisee;

    @Column(nullable = false)
    private Double duree;

    @Column(nullable = false, name = "date_mesure")
    private LocalDateTime dateMesure;

    @PrePersist
    protected void onCreate() {
        if (dateMesure == null) {
            dateMesure = LocalDateTime.now();
        }
    }
}
