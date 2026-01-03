package com.ffx64.api.dto;

public record PlayerStatsResponseDTO(
        String id,
        String guid,
        String username,
        Integer admin,
        Integer premium,
        String premium_start_at,
        String premium_expire_at,
        Boolean is_active,
        Boolean is_banned,
        String last_server_id,
        String last_login,
        String platform,
        String updated_at,
        String created_at,
        StatsResponseDTO stats) {
}
