package com.ffx64.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffx64.api.dto.ServerRequestDTO;
import com.ffx64.api.dto.ServerResponseDTO;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ServersService {

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
        this.URL = URI.create(BASE_URL + "/api/v1/servers");
    }

    @CacheEvict(value = { "servers_all", "servers_by_id" }, allEntries = true)
    public ServerResponseDTO save(ServerRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (user.getIs_admin().equals(true)) {
            throw new UnauthorizedException("Você não tem permissão para realizar esta ação.");
        }

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URL)
                    .header("Authorization", SECRET_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(dto)))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), ServerResponseDTO.class);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "servers_all", key = "#root.methodName")
    public List<ServerResponseDTO> getAll() {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (user.getIs_admin().equals(true)) {
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

            return fromJsonToDTO(response.body(), new TypeReference<List<ServerResponseDTO>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @Cacheable(value = "servers_by_id", key = "#id")
    public ServerResponseDTO getById(String id) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (user.getIs_admin().equals(true)) {
            throw new UnauthorizedException("Você não tem permissão para realizar esta ação.");
        }

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

            return fromJsonToDTO(response.body(), ServerResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @CacheEvict(value = { "servers_all", "servers_by_id" }, allEntries = true)
    public ServerResponseDTO update(String id, ServerRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (user.getIs_admin().equals(true)) {
            throw new UnauthorizedException("Você não tem permissão para realizar esta ação.");
        }

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + id))
                    .header("Authorization", SECRET_KEY)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(dto)))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return fromJsonToDTO(response.body(), ServerResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException();
        }
    }

    @CacheEvict(value = { "servers_all", "servers_by_id" }, allEntries = true)
    public void delete(String id) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado"));

        if (user.getIs_admin().equals(true)) {
            throw new UnauthorizedException("Você não tem permissão para realizar esta ação.");
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + id))
                    .header("Authorization", SECRET_KEY)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                ApiErrorResponseDTO error = mapper.readValue(response.body(), ApiErrorResponseDTO.class);
                throw new GenericException(error.message(), error.status());
            }

            return;
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
