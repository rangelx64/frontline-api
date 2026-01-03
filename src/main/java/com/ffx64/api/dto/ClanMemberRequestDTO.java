package com.ffx64.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClanMemberRequestDTO(
        @NotNull(message = "O ID do clã é obrigatório") UUID clan_id,
        @NotNull(message = "O ID do usuário é obrigatório") UUID user_id,
        Boolean is_owner,
        LocalDateTime joined_at) {
}
