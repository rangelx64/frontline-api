package com.ffx64.api.controller;

import com.ffx64.api.dto.LeaderboardResponseDTO;
import com.ffx64.api.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService service;

    @GetMapping
    public ResponseEntity<List<LeaderboardResponseDTO>> getLeaderboard() {
        return ResponseEntity.ok(service.getLeaderboard());
    }

    @GetMapping("/vehicle")
    public ResponseEntity<List<LeaderboardResponseDTO>> getLeaderboardByVehicle() {
        return ResponseEntity.ok(service.getLeaderboardByVehicle());
    }

    @GetMapping("/headshots")
    public ResponseEntity<List<LeaderboardResponseDTO>> getLeaderboardByHeadshots() {
        return ResponseEntity.ok(service.getLeaderboardByHeadshots());
    }
}
