package com.ffx64.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffx64.api.dto.RoundListResponseDTO;
import com.ffx64.api.dto.RoundResponseDTO;
import com.ffx64.api.dto.RoundScoreboardDTO;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.exception.ApiErrorResponseDTO;
import com.ffx64.api.exception.GenericException;
import com.ffx64.api.exception.InternalServerErrorException;
import com.ffx64.api.exception.UnauthorizedException;
import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RoundsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${http.gamestats.api.url}")
    private String BASE_URL;

    @Value("${http.gamestats.api.secret}")
    private String SECRET_KEY;

    private URI URL;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        this.URL = URI.create(BASE_URL + "/api/v1/rounds");
    }

    public RoundListResponseDTO meByServerId(String serverId, Integer limit, Integer offset) {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/server/" + serverId + "/player/" + user.getId() + "?limit=" + limit
                            + "&offset=" + offset))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), RoundListResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "rounds_scoreboard", key = "#id", sync = true)
    public List<RoundScoreboardDTO> scoreboard(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + id + "/scoreboard"))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), new TypeReference<List<RoundScoreboardDTO>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

    }

    public List<RoundResponseDTO> getAll() {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (!Boolean.TRUE.equals(user.getIs_admin())) {
            throw new UnauthorizedException("Você não tem permissão para realizar esta ação.");
        }

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

            return fromJsonToDTO(response.body(), new TypeReference<List<RoundResponseDTO>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "rounds_by_id", key = "#id", sync = true)
    public RoundResponseDTO getById(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + id))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), RoundResponseDTO.class);
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

    private <T> T fromJsonToDTO(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON (coleção)", e);
        }
    }
}
