package com.ffx64.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ServerRequestDTO(
        @NotBlank(message = "O nome do servidor é obrigatório") String name) {
}
