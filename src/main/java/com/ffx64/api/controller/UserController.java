package com.ffx64.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.dto.UserRequestDTO;
import com.ffx64.api.dto.UserResponseDTO;
import com.ffx64.api.service.interfaces.UserServiceInterface;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me() {
        return ResponseEntity.ok(userService.get());
    }

    @GetMapping("/me/stats")
    public ResponseEntity<PlayerStatsResponseDTO> stats() {
        return ResponseEntity.ok(userService.stats());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> update(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.update(userRequestDTO));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete() {
        userService.delete();
        return ResponseEntity.noContent().build();
    }
}
