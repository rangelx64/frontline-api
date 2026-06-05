package com.ffx64.api.controller;

import com.ffx64.api.dto.ClanMemberRequestDTO;
import com.ffx64.api.dto.ClanMemberResponseDTO;
import com.ffx64.api.service.ClanMembersService;
import com.ffx64.api.service.interfaces.ClanMemberServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clan-members")
public class ClanMemberController {

    private final ClanMemberServiceInterface clanMemberService;

    public ClanMemberController(ClanMembersService clanMemberService) {
        this.clanMemberService = clanMemberService;
    }

    @PostMapping("/create")
    @Operation(summary = "Cria um novo membro de clã")
    public ResponseEntity<ClanMemberResponseDTO> createClanMember(
            @Valid @RequestBody ClanMemberRequestDTO clanMemberRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clanMemberService.save(clanMemberRequestDTO));
    }

    @GetMapping("/all")
    @Operation(summary = "Retorna todos os membros de clã")
    public ResponseEntity<Page<ClanMemberResponseDTO>> getAllClanMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(clanMemberService.findAll(pageable(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um membro de clã por ID")
    public ResponseEntity<ClanMemberResponseDTO> getClanMemberById(
            @PathVariable UUID id) {
        return clanMemberService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clan/{clanId}")
    @Operation(summary = "Retorna todos os membros de um clã por ID do clã")
    public ResponseEntity<List<ClanMemberResponseDTO>> getClanMembersByClanId(
            @PathVariable UUID clanId) {
        return ResponseEntity.ok(clanMemberService.findByClanId(clanId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Retorna todos os clãs de um usuário por ID do usuário")
    public ResponseEntity<List<ClanMemberResponseDTO>> getClanMembersByUserId(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(clanMemberService.findByUserId(userId));
    }

    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "joined_at"));
    }
}
