package com.ffx64.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClanMemberResponseDTO(
        UUID id,
        UUID clan_id,
        UUID user_id,
        Boolean is_owner,
        LocalDateTime joined_at) {
}
