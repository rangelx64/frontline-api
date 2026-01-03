package com.ffx64.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.service.PlayersService;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    @Autowired
    private PlayersService service;

    @GetMapping("/{bohemia_guid}")
    public ResponseEntity<PlayerStatsResponseDTO> getByGuid(@PathVariable String bohemia_guid) {
        return ResponseEntity.ok(service.getByGuid(bohemia_guid));
    }
}
