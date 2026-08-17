package com.mbemnova.recensement.service;

import com.mbemnova.recensement.dto.MenageRequestDTO;
import com.mbemnova.recensement.dto.MenageResponseDTO;
import com.mbemnova.recensement.dto.StatistiquesDTO;
import com.mbemnova.recensement.entity.Menage;
import com.mbemnova.recensement.exception.ResourceNotFoundException;
import com.mbemnova.recensement.mapper.MenageMapper;
import com.mbemnova.recensement.repository.MenageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MenageService {


    /**
     * Un menage est considere comme "surpeuple" au-dela de ce nombre de personnes.
     * Seuil retenu par MbemNova pour ce recensement (peut etre ajuste par l'Etat).
     */
    private static final int SEUIL_SURPEUPLEMENT = 6;

    private final MenageRepository menageRepository;
    private final MenageMapper menageMapper;

    public MenageResponseDTO creer(MenageRequestDTO requestDTO) {
        Menage menage = menageMapper.toEntity(requestDTO);
        Menage sauvegarde = menageRepository.save(menage);
        return menageMapper.toResponseDTO(sauvegarde);
    }

    public List<MenageResponseDTO> listerTous() {
        return menageRepository.findAll().stream()
                .map(menageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void supprimer(Long id) {
        if (!menageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aucun menage trouve avec l'identifiant : " + id);
        }
        menageRepository.deleteById(id);
    }

    public StatistiquesDTO calculerStatistiques() {
        List<Menage> tousLesMenages = menageRepository.findAll();

        if (tousLesMenages.isEmpty()) {
            return StatistiquesDTO.builder()
                    .populationTotale(0)
                    .nombreMenages(0)
                    .tailleMoyenneMenage(0)
                    .zonePlusPeuplee(null)
                    .zoneAgeMoyenLePlusBas(null)
                    .zoneAgeMoyenLePlusEleve(null)
                    .zoneTauxSurpeuplementLePlusEleve(null)
                    .typeLogementDominantParZone(Map.of())
                    .build();
        }

        // 1. Population totale recensee
        long populationTotale = tousLesMenages.stream()
                .mapToLong(Menage::getNombrePersonnes)
                .sum();

        // 2. Nombre de menages enregistres
        long nombreMenages = tousLesMenages.size();

        // 3. Taille moyenne d'un menage
        double tailleMoyenneMenage = (double) populationTotale / nombreMenages;

        // Regroupement des menages par zone (reutilise pour les stats 4 a 8)
        Map<String, List<Menage>> menagesParZone = tousLesMenages.stream()
                .collect(Collectors.groupingBy(Menage::getZone));

        // 4. Zone avec le plus grand nombre d'habitants
        StatistiquesDTO.ZoneValeurDTO zonePlusPeuplee = menagesParZone.entrySet().stream()
                .map(e -> new StatistiquesDTO.ZoneValeurDTO(
                        e.getKey(),
                        e.getValue().stream().mapToDouble(Menage::getNombrePersonnes).sum()))
                .max(Comparator.comparingDouble(StatistiquesDTO.ZoneValeurDTO::getValeur))
                .orElse(null);

        // 5. Zone avec l'age moyen le plus bas
        StatistiquesDTO.ZoneValeurDTO zoneAgeMoyenLePlusBas = menagesParZone.entrySet().stream()
                .map(e -> new StatistiquesDTO.ZoneValeurDTO(
                        e.getKey(),
                        e.getValue().stream().mapToDouble(Menage::getAgeMoyen).average().orElse(0)))
                .min(Comparator.comparingDouble(StatistiquesDTO.ZoneValeurDTO::getValeur))
                .orElse(null);

        // 6. Zone avec l'age moyen le plus eleve
        StatistiquesDTO.ZoneValeurDTO zoneAgeMoyenLePlusEleve = menagesParZone.entrySet().stream()
                .map(e -> new StatistiquesDTO.ZoneValeurDTO(
                        e.getKey(),
                        e.getValue().stream().mapToDouble(Menage::getAgeMoyen).average().orElse(0)))
                .max(Comparator.comparingDouble(StatistiquesDTO.ZoneValeurDTO::getValeur))
                .orElse(null);

        // 7. Zone avec le taux de surpeuplement le plus eleve
        // (pourcentage de menages de la zone qui depassent le seuil de surpeuplement)
        StatistiquesDTO.ZoneValeurDTO zoneTauxSurpeuplementLePlusEleve = menagesParZone.entrySet().stream()
                .map(e -> {
                    long total = e.getValue().size();
                    long surpeuples = e.getValue().stream()
                            .filter(m -> m.getNombrePersonnes() > SEUIL_SURPEUPLEMENT)
                            .count();
                    double taux = total == 0 ? 0 : (surpeuples * 100.0) / total;
                    return new StatistiquesDTO.ZoneValeurDTO(e.getKey(), taux);
                })
                .max(Comparator.comparingDouble(StatistiquesDTO.ZoneValeurDTO::getValeur))
                .orElse(null);

        // 8. Type de logement dominant, par zone
        Map<String, String> typeLogementDominantParZone = menagesParZone.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> typeDominant(e.getValue())
                ));

        return StatistiquesDTO.builder()
                .populationTotale(populationTotale)
                .nombreMenages(nombreMenages)
                .tailleMoyenneMenage(arrondir(tailleMoyenneMenage))
                .zonePlusPeuplee(zonePlusPeuplee)
                .zoneAgeMoyenLePlusBas(arrondirZoneValeur(zoneAgeMoyenLePlusBas))
                .zoneAgeMoyenLePlusEleve(arrondirZoneValeur(zoneAgeMoyenLePlusEleve))
                .zoneTauxSurpeuplementLePlusEleve(arrondirZoneValeur(zoneTauxSurpeuplementLePlusEleve))
                .typeLogementDominantParZone(typeLogementDominantParZone)
                .build();
    }

    private String typeDominant(List<Menage> menagesDeZone) {
        return menagesDeZone.stream()
                .collect(Collectors.groupingBy(Menage::getTypeLogement, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Inconnu");
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }

    private StatistiquesDTO.ZoneValeurDTO arrondirZoneValeur(StatistiquesDTO.ZoneValeurDTO zv) {
        if (zv == null || zv.getValeur() == null) {
            return zv;
        }
        zv.setValeur(arrondir(zv.getValeur()));
        return zv;
    }
}
