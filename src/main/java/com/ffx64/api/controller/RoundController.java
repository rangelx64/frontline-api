package com.ffx64.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ffx64.api.dto.RoundListResponseDTO;
import com.ffx64.api.dto.RoundResponseDTO;
import com.ffx64.api.dto.RoundScoreboardDTO;
import com.ffx64.api.service.RoundsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rounds")
public class RoundController {

    @Autowired
    private RoundsService service;

    /*
     * AUTH USER
     */
    @GetMapping("/by-server/{serverId}")
    public ResponseEntity<RoundListResponseDTO> meByServerId(@PathVariable String serverId,
            @RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "0") Integer offset) {

        return ResponseEntity.ok(service.meByServerId(serverId, limit, offset));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoundResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/{id}/scoreboard")
    public ResponseEntity<List<RoundScoreboardDTO>> getScoreboard(@PathVariable String id) {
        return ResponseEntity.ok(service.scoreboard(id));
    }

    /*
     * ADMIN USER
     */
    @GetMapping("/admin")
    public ResponseEntity<List<RoundResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
