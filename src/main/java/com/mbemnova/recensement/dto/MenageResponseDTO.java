package com.mbemnova.recensement.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Donnees renvoyees au frontend lorsqu'on affiche un menage.
 * Inclut l'identifiant et les deux dates automatiques.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenageResponseDTO {

    private Long id;
    private String chefMenage;
    private String zone;
    private Integer nombrePersonnes;
    private Double ageMoyen;
    private String typeLogement;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereModification;
}
