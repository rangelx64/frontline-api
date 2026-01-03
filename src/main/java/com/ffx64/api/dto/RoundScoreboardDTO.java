package com.ffx64.api.dto;

public record RoundScoreboardDTO(
        String id,
        String roundId,
        String serverId,
        String playerId,
        String team,
        Integer TotalKills,
        Integer TotalDeaths,
        Integer TotalSuicides,
        Float AverageKillDistance,
        Integer TotalTeamKills,
        Integer TotalHeadshots,
        Integer TotalVehicleKills,
        String MostUsedWeapon,
        String MostHitZone,
        Long CreatedAt,
        Long UpdatedAt) {
}
