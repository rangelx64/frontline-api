package com.ffx64.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.exception.ApiErrorResponseDTO;
import com.ffx64.api.exception.GenericException;
import com.ffx64.api.exception.InternalServerErrorException;

import jakarta.annotation.PostConstruct;

@Service
public class PlayersService {

    @Value("${http.gamestats.api.url}")
    private String BASE_URL;

    @Value("${http.gamestats.api.secret}")
    private String SECRET_KEY;

    private URI URL;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        this.URL = URI.create(BASE_URL + "/api/v1/players");
    }

    @Cacheable(value = "players_profile", key = "#bohemia_guid", sync = true)
    public PlayerStatsResponseDTO getByGuid(String bohemia_guid) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + bohemia_guid + "/stats"))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), PlayerStatsResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    private <T> T fromJsonToDTO(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON para " + clazz.getSimpleName(), e);
        }
    }
}
