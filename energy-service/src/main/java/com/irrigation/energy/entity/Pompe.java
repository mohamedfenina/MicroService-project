package com.irrigation.energy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pompes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pompe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Column(nullable = false)
    private Double puissance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutPompe statut;

    @Column(nullable = false, name = "date_mise_en_service")
    private LocalDateTime dateMiseEnService;

    @Column(nullable = false, length = 20, name = "energy_status")
    private String energyStatus = "Normal";

    public enum StatutPompe {
        ACTIVE,
        INACTIVE,
        MAINTENANCE
    }

    @PrePersist
    protected void onCreate() {
        if (dateMiseEnService == null) {
            dateMiseEnService = LocalDateTime.now();
        }
    }
}
