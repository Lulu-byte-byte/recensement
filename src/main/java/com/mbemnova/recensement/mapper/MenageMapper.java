package com.mbemnova.recensement.mapper;

import com.mbemnova.recensement.dto.MenageRequestDTO;
import com.mbemnova.recensement.dto.MenageResponseDTO;
import com.mbemnova.recensement.entity.Menage;
import org.springframework.stereotype.Component;


@Component
public class MenageMapper {


    public Menage toEntity(MenageRequestDTO dto) {
        return Menage.builder()
                .chefMenage(dto.getChefMenage())
                .zone(dto.getZone())
                .nombrePersonnes(dto.getNombrePersonnes())
                .ageMoyen(dto.getAgeMoyen())
                .typeLogement(dto.getTypeLogement())
                .build();
    }

    public MenageResponseDTO toResponseDTO(Menage menage) {
        return MenageResponseDTO.builder()
                .id(menage.getId())
                .chefMenage(menage.getChefMenage())
                .zone(menage.getZone())
                .nombrePersonnes(menage.getNombrePersonnes())
                .ageMoyen(menage.getAgeMoyen())
                .typeLogement(menage.getTypeLogement())
                .dateCreation(menage.getDateCreation())
                .dateDerniereModification(menage.getDateDerniereModification())
                .build();
    }
}
