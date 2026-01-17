package com.irrigation.water.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "debits_mesures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitMesure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "pompe_id")
    private Long pompeId;

    @Column(nullable = false)
    private Double debit;

    @Column(nullable = false, name = "date_mesure")
    private LocalDateTime dateMesure;

    @Column(nullable = false, length = 20)
    private String unite;

    @PrePersist
    protected void onCreate() {
        if (dateMesure == null) {
            dateMesure = LocalDateTime.now();
        }
    }
}
