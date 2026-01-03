package com.ffx64.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccessLogRequestDTO(
        UUID user_id,

        @NotNull(message = "O endereço IP é obrigatório") String ip_address,

        String user_agent,

        String method,

        String endpoint,

        Integer status_code,

        Integer response_time_ms,

        LocalDateTime created_at) {
}
