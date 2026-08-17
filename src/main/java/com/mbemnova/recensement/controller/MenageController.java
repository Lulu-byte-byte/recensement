package com.mbemnova.recensement.controller;


import com.mbemnova.recensement.dto.MenageRequestDTO;
import com.mbemnova.recensement.dto.MenageResponseDTO;
import com.mbemnova.recensement.dto.StatistiquesDTO;
import com.mbemnova.recensement.service.MenageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/menages")
    @RequiredArgsConstructor
    @Tag(name = "Menages", description = "Gestion du recensement des menages du Cameroun")
    public class MenageController {

        private final MenageService menageService;

        @PostMapping
        @Operation(summary = "Enregistrer un nouveau menage",
                responses = {
                        @ApiResponse(responseCode = "201", description = "Menage cree avec succes"),
                        @ApiResponse(responseCode = "400", description = "Donnees invalides")
                })
        public ResponseEntity<MenageResponseDTO> creer(@Valid @RequestBody MenageRequestDTO requestDTO) {
            MenageResponseDTO cree = menageService.creer(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(cree);
        }

        @GetMapping
        @Operation(summary = "Recuperer la liste de tous les menages enregistres",
                responses = @ApiResponse(responseCode = "200", description = "Liste recuperee avec succes"))
        public ResponseEntity<List<MenageResponseDTO>> listerTous() {
            return ResponseEntity.ok(menageService.listerTous());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer un menage a partir de son identifiant",
                responses = {
                        @ApiResponse(responseCode = "204", description = "Menage supprime avec succes"),
                        @ApiResponse(responseCode = "404", description = "Menage introuvable")
                })
        public ResponseEntity<Void> supprimer(@PathVariable Long id) {
            menageService.supprimer(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/statistiques")
        @Operation(summary = "Recuperer les huit statistiques calculees sur le recensement",
                responses = @ApiResponse(responseCode = "200", description = "Statistiques calculees avec succes"))
        public ResponseEntity<StatistiquesDTO> statistiques() {
            return ResponseEntity.ok(menageService.calculerStatistiques());
        }
}
