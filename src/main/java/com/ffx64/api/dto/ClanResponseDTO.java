package com.ffx64.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClanResponseDTO(
        UUID id,
        String name,
        String tag,
        String photo,
        String description,
        LocalDateTime created_at,
        LocalDateTime updated_at) {
}
