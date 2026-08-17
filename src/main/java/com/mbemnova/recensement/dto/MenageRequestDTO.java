package com.mbemnova.recensement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Donnees envoyees par le frontend lors de l'enregistrement d'un menage.
 * Ne contient que les champs saisis par l'agent recenseur.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenageRequestDTO {

    @NotBlank(message = "Le nom du chef de menage est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du chef de menage doit contenir entre 2 et 100 caracteres")
    private String chefMenage;

    @NotBlank(message = "La zone est obligatoire")
    @Size(min = 2, max = 100, message = "La zone doit contenir entre 2 et 100 caracteres")
    private String zone;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Un menage doit compter au moins 1 personne")
    @Max(value = 30, message = "Le nombre de personnes ne peut pas depasser 30")
    private Integer nombrePersonnes;

    @NotNull(message = "L'age moyen est obligatoire")
    @DecimalMin(value = "0.0", message = "L'age moyen ne peut pas etre negatif")
    @DecimalMax(value = "120.0", message = "L'age moyen ne peut pas depasser 120 ans")
    private Double ageMoyen;

    @NotBlank(message = "Le type de logement est obligatoire")
    @Size(min = 2, max = 50, message = "Le type de logement doit contenir entre 2 et 50 caracteres")
    private String typeLogement;
}
