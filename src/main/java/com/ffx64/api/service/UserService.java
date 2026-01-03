package com.ffx64.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.dto.UserRequestDTO;
import com.ffx64.api.dto.UserResponseDTO;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.exception.ApiErrorResponseDTO;
import com.ffx64.api.exception.GenericException;
import com.ffx64.api.exception.InternalServerErrorException;
import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.service.interfaces.UserServiceInterface;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserServiceInterface {

    @Value("${http.gamestats.api.url}")
    private String BASE_URL;

    @Value("${http.gamestats.api.secret}")
    private String SECRET_KEY;

    private URI URL;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void init() {
        this.URL = URI.create(BASE_URL + "/api/v1/players");
    }

    public UserResponseDTO get() {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));
        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        user.setUsername(userRequestDTO.username());
        user.setBohemia_guid(userRequestDTO.bohemia_guid());
        user.setEmail(userRequestDTO.email());
        user.setAccept_terms(userRequestDTO.accept_terms());
        user.setPhoto(userRequestDTO.photo());
        user.setBanner_photo(userRequestDTO.banner_photo());
        user.setDescription(userRequestDTO.description());
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updated = userRepository.save(user);

        return toResponseDTO(updated);
    }

    @Transactional
    public void delete() {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        user.setDeletedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);
    }

    public PlayerStatsResponseDTO stats() {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + user.getBohemia_guid() + "/stats"))
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

    private UserResponseDTO toResponseDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getBohemia_guid(),
                user.getEmail(),
                user.getAcceptTerms(),
                user.getPhoto(),
                user.getBanner_photo(),
                user.getDescription(),
                user.getIs_admin(),
                user.getDonator(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt());
    }
}
