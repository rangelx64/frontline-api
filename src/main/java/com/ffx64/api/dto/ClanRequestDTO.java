package com.ffx64.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ClanRequestDTO(
        @NotBlank String name,
        @NotBlank String tag,
        String photo,
        String description) {
}
