package com.irrigation.water.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservoirs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservoir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, name = "capacite_totale")
    private Double capaciteTotale;

    @Column(nullable = false, name = "volume_actuel")
    private Double volumeActuel;

    @Column(nullable = false, length = 255)
    private String localisation;
}
