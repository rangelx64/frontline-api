package com.ffx64.api.controller;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import com.ffx64.api.service.AccessLogService;
import com.ffx64.api.service.interfaces.AccessLogServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/access-logs")
public class AccessLogController {

    private final AccessLogServiceInterface accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @PostMapping("/create")
    @Operation(summary = "Registra um novo log de acesso")
    public ResponseEntity<AccessLogResponseDTO> createAccessLog(
            @Valid @RequestBody AccessLogRequestDTO accessLogRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accessLogService.save(accessLogRequestDTO));
    }

    @GetMapping("/all")
    @Operation(summary = "Retorna todos os logs de acesso")
    public ResponseEntity<List<AccessLogResponseDTO>> getAllAccessLogs() {
        return ResponseEntity.ok(accessLogService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um log de acesso por ID")
    public ResponseEntity<AccessLogResponseDTO> getAccessLogById(
            @PathVariable Long id) {
        return accessLogService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Retorna todos os logs de acesso de um usuário por ID")
    public ResponseEntity<List<AccessLogResponseDTO>> getAccessLogsByUserId(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(accessLogService.findByUserId(userId));
    }

    @GetMapping("/ip/{ipAddress}")
    @Operation(summary = "Retorna todos os logs de acesso de um endereço IP")
    public ResponseEntity<List<AccessLogResponseDTO>> getAccessLogsByIpAddress(
            @PathVariable String ipAddress) {
        return ResponseEntity.ok(accessLogService.findByIpAddress(ipAddress));
    }
}
