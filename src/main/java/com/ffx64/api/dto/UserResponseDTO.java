package com.ffx64.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String bohemia_guid,
        String email,
        Boolean accept_terms,
        String photo,
        String banner_photo,
        String description,
        Boolean is_admin,
        Boolean donator,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        LocalDateTime deleted_at) {
}
