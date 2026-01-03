package com.ffx64.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ffx64.api.dto.LoginRequestDTO;
import com.ffx64.api.dto.LoginResponseDTO;
import com.ffx64.api.dto.RegisterRequestDTO;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.exception.PlayerVerificationException;
import com.ffx64.api.exception.TermsNotAccepted;
import com.ffx64.api.exception.UserExistsException;
import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.security.JWTService;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${http.gamestats.api.secret}")
    private String SECRET_KEY;

    @Value("${http.gamestats.api.url}")
    private String BASE_URL;

    private URI URL;

    @PostConstruct
    private void init() {
        this.URL = URI.create(BASE_URL + "/api/v1/players");
    }

    public Boolean register(RegisterRequestDTO request) {
        if (!request.accept_terms()) {
            throw new TermsNotAccepted();
        }

        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new UserExistsException("Email já está em uso.");
        });

        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new UserExistsException("Nome de usuário já está em uso.");
        });

        userRepository.findByBohemia_guid(request.bohemia_guid()).ifPresent(user -> {
            throw new UserExistsException("Bohemia GUID já vinculado a outra conta.");
        });

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + request.bohemia_guid()))
                    .header("Authorization", SECRET_KEY)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Tratar todos os status
            if (response.statusCode() == 200) {
                // Usuário validado, salvar
                UserEntity user = new UserEntity();
                user.setUsername(request.username());
                user.setEmail(request.email());
                user.setActive(true);
                user.setBohemia_guid(request.bohemia_guid());
                user.setPassword(passwordEncoder.encode(request.password()));
                user.setAccept_terms(request.accept_terms());
                userRepository.save(user);

                return true;
            } else if (response.statusCode() == 404) {
                throw new PlayerVerificationException("GUID não encontrado no sistema externo.");
            } else {
                throw new PlayerVerificationException(
                        "Falha na verificação do jogador. Status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new PlayerVerificationException("Ocorreu um erro ao verificar jogador.");
        }
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (user.getDeletedAt() != null) {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            if (user.getDeletedAt().isBefore(thirtyDaysAgo)) {
                // Deletado há mais de 30 dias -> não encontrado
                throw new UserNotFoundException("Usuário não encontrado");
            } else {
                // Deletado há menos de 30 dias -> reativa conta
                user.setDeletedAt(null);
                user.setActive(true);
                userRepository.save(user);
            }
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBohemia_guid(),
                user.getPhoto(),
                user.getBanner_photo(), user.getIs_admin(),
                token);
    }

    // Logout - não invalida o token no servidor (JWT é stateless)
    public String logout(String token) {
        // Opcionalmente: colocar o token em blacklist (DB, Redis, etc)
        return "Logout realizado com sucesso (token expirará naturalmente).";
    }
}
