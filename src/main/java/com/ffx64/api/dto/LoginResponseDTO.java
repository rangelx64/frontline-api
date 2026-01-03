package com.ffx64.api.dto;

import java.util.UUID;

public record LoginResponseDTO(
        UUID id,
        String username,
        String email,
        String bohemia_guid,
        String photo,
        String banner_photo,
        Boolean is_admin,
        String token) {
}
