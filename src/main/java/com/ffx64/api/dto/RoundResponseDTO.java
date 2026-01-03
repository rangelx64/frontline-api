package com.ffx64.api.dto;

public record RoundResponseDTO(
        String id,
        String server_id,
        String current_mode,
        String mission_header,
        String status,
        String winner_faction,
        String ended_at,
        String start_at,
        String created_at) {
}
