package com.ffx64.api.controller;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import com.ffx64.api.service.AccessLogService;
import com.ffx64.api.service.interfaces.AccessLogServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<AccessLogResponseDTO>> getAllAccessLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(accessLogService.findAll(pageable(page, size)));
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
    public ResponseEntity<Page<AccessLogResponseDTO>> getAccessLogsByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(accessLogService.findByUserId(userId, pageable(page, size)));
    }

    @GetMapping("/ip/{ipAddress}")
    @Operation(summary = "Retorna todos os logs de acesso de um endereço IP")
    public ResponseEntity<Page<AccessLogResponseDTO>> getAccessLogsByIpAddress(
            @PathVariable String ipAddress,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(accessLogService.findByIpAddress(ipAddress, pageable(page, size)));
    }

    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "created_at"));
    }
}
