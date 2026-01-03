package com.ffx64.api.controller;

import com.ffx64.api.dto.ClanRequestDTO;
import com.ffx64.api.dto.ClanResponseDTO;
import com.ffx64.api.service.interfaces.ClanServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clans")
public class ClanController {

    private final ClanServiceInterface clanService;

    public ClanController(ClanServiceInterface clanService) {
        this.clanService = clanService;
    }

    @PostMapping("/create")
    @Operation(summary = "Cria um novo clã")
    public ResponseEntity<ClanResponseDTO> createClan(
            @RequestBody ClanRequestDTO clanRequestDTO) {
        ClanResponseDTO clanResponseDTO = clanService.save(clanRequestDTO);
        if (clanResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    clanResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Retorna todos os clãs")
    public ResponseEntity<List<ClanResponseDTO>> getAllClans() {
        List<ClanResponseDTO> clanResponseDTOs = clanService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(clanResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um clã pelo ID")
    public ResponseEntity<ClanResponseDTO> getClanById(@PathVariable UUID id) {
        Optional<ClanResponseDTO> clanResponseDTO = clanService.findById(id);
        if (clanResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    clanResponseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Retorna um clã pelo tag")
    public ResponseEntity<ClanResponseDTO> getClanByTag(
            @PathVariable String tag) {
        Optional<ClanResponseDTO> clanResponseDTO = clanService.findByTag(tag);
        if (clanResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    clanResponseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Retorna um clã pelo nome")
    public ResponseEntity<ClanResponseDTO> getClanByName(
            @PathVariable String name) {
        Optional<ClanResponseDTO> clanResponseDTO = clanService.findByName(
                name);
        if (clanResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    clanResponseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um clã pelo ID")
    public ResponseEntity<Boolean> deleteClan(@PathVariable UUID id) {
        Boolean deleted = clanService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
