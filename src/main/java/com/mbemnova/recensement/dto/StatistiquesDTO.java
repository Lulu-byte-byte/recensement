package com.mbemnova.recensement.dto;

import lombok.*;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiquesDTO {


    // 1. Population totale recensee
    private long populationTotale;

    // 2. Nombre de menages enregistres
    private long nombreMenages;

    // 3. Taille moyenne d'un menage
    private double tailleMoyenneMenage;

    // 4. Zone avec le plus grand nombre d'habitants
    private ZoneValeurDTO zonePlusPeuplee;

    // 5. Zone avec l'age moyen le plus bas
    private ZoneValeurDTO zoneAgeMoyenLePlusBas;

    // 6. Zone avec l'age moyen le plus eleve
    private ZoneValeurDTO zoneAgeMoyenLePlusEleve;

    // 7. Zone avec le taux de surpeuplement le plus eleve (en %)
    private ZoneValeurDTO zoneTauxSurpeuplementLePlusEleve;

    // 8. Type de logement dominant, par zone
    private Map<String, String> typeLogementDominantParZone;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ZoneValeurDTO {
        private String zone;
        private Double valeur;
    }
}
