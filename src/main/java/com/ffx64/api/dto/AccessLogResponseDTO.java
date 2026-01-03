package com.ffx64.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessLogResponseDTO(
        Long id,
        UUID user_id,
        String ip_address,
        String user_agent,
        String method,
        String endpoint,
        Integer status_code,
        Integer response_time_ms,
        LocalDateTime created_at) {
}
