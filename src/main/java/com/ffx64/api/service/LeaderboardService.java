package com.ffx64.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffx64.api.dto.LeaderboardResponseDTO;
import com.ffx64.api.dto.LeaderboardStatsDTO;
import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.exception.ApiErrorResponseDTO;
import com.ffx64.api.exception.GenericException;
import com.ffx64.api.exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardService.class);

    @Value("${http.gamestats.api.url}")
    private String BASE_URL;

    @Value("${http.gamestats.api.secret}")
    private String SECRET_KEY;

    private final PlayersService playersService;
    private URI URL;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public LeaderboardService(PlayersService playersService) {
        this.playersService = playersService;
    }

    @PostConstruct
    private void init() {
        this.URL = URI.create(BASE_URL + "/api/v1/leaderboard");
    }

    @Cacheable(value = "leaderboard_general", sync = true)
    public List<LeaderboardResponseDTO> getLeaderboard() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URL)
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            List<LeaderboardStatsDTO> stats = fromJsonToList(response.body());
            return stats.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "leaderboard_vehicle", sync = true)
    public List<LeaderboardResponseDTO> getLeaderboardByVehicle() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/vehicle"))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            List<LeaderboardStatsDTO> stats = fromJsonToList(response.body());
            return stats.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "leaderboard_headshots", sync = true)
    public List<LeaderboardResponseDTO> getLeaderboardByHeadshots() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/headshots"))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            List<LeaderboardStatsDTO> stats = fromJsonToList(response.body());
            return stats.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    private List<LeaderboardStatsDTO> fromJsonToList(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<LeaderboardStatsDTO>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON para List<LeaderboardStatsDTO>", e);
        }
    }

    private LeaderboardResponseDTO toResponseDTO(LeaderboardStatsDTO stats) {
        String username = "Unknown";
        try {
            PlayerStatsResponseDTO player = playersService.getByGuid(stats.player_id());
            username = player.username();
        } catch (Exception e) {
            LOGGER.warn("Player não encontrado para GUID: {}", stats.player_id());
        }

        return new LeaderboardResponseDTO(
                stats.id(),
                username,
                stats.level(),
                stats.xp(),
                stats.kills(),
                stats.deaths(),
                stats.grenades_thrown(),
                stats.friendly_fire_kills(),
                stats.friendly_fire_deaths(),
                stats.headshots_made(),
                stats.headshots_taken(),
                stats.vehicle_kills(),
                stats.vehicle_deaths(),
                stats.longest_kill_distance(),
                stats.average_kill_distance(),
                stats.average_death_distance(),
                stats.weapons_most_used(),
                stats.vehicle_most_used(),
                stats.hit_zones_most_killed(),
                stats.hit_zones_most_died(),
                stats.ratio_kdr(),
                stats.ratio_headshot(),
                stats.ratio_friendly_fire(),
                stats.max_kill_distance(),
                stats.updated_at(),
                stats.created_at());
    }
}
