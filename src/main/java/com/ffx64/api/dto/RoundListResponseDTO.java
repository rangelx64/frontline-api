package com.ffx64.api.dto;

import java.util.List;

public record RoundListResponseDTO(
        Integer total,
        List<RoundResponseDTO> data) {
}
