package com.mbemnova.recensement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "menages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Menage {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "Le nom du chef de menage est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom du chef de menage doit contenir entre 2 et 100 caracteres")
        @Column(nullable = false, length = 100)
        private String chefMenage;

        @NotBlank(message = "La zone est obligatoire")
        @Size(min = 2, max = 100, message = "La zone doit contenir entre 2 et 100 caracteres")
        @Column(nullable = false, length = 100)
        private String zone;

        @NotNull(message = "Le nombre de personnes est obligatoire")
        @Min(value = 1, message = "Un menage doit compter au moins 1 personne")
        @Max(value = 30, message = "Le nombre de personnes ne peut pas depasser 30")
        @Column(nullable = false)
        private Integer nombrePersonnes;

        @NotNull(message = "L'age moyen est obligatoire")
        @DecimalMin(value = "0.0", message = "L'age moyen ne peut pas etre negatif")
        @DecimalMax(value = "120.0", message = "L'age moyen ne peut pas depasser 120 ans")
        @Column(nullable = false)
        private Double ageMoyen;

        @NotBlank(message = "Le type de logement est obligatoire")
        @Size(min = 2, max = 50, message = "Le type de logement doit contenir entre 2 et 50 caracteres")
        @Column(nullable = false, length = 50)
        private String typeLogement;

        @Column(nullable = false, updatable = false)
        private LocalDateTime dateCreation;

        @Column(nullable = false)
        private LocalDateTime dateDerniereModification;

        @PrePersist
        protected void onCreate() {
            LocalDateTime now = LocalDateTime.now();
            this.dateCreation = now;
            this.dateDerniereModification = now;
        }

        @PreUpdate
        protected void onUpdate() {
            this.dateDerniereModification = LocalDateTime.now();
        }
    }


