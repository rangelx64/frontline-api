package com.ffx64.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ffx64.api.dto.ServerRequestDTO;
import com.ffx64.api.dto.ServerResponseDTO;
import com.ffx64.api.service.ServersService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servers")
public class ServerController {

    @Autowired
    private ServersService service;

    /*
     * ADMIN USER
     */

    @PostMapping
    public ResponseEntity<ServerResponseDTO> create(@RequestBody ServerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<List<ServerResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServerResponseDTO> update(@PathVariable String id,
            @RequestBody ServerRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
