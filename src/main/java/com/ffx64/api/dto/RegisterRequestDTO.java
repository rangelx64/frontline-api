package com.ffx64.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome de usuário é obrigatório") String username,
        @NotBlank(message = "O GUID é obrigatório") String bohemia_guid,
        @NotBlank(message = "O email é obrigatório") String email,
        @NotBlank(message = "A senha é obrigatória") String password,
        @NotBlank(message = "É necessário aceitar os termos") Boolean accept_terms) {
}
