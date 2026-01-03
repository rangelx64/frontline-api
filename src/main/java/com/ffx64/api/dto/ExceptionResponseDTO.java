package com.ffx64.api.dto;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(
        String error,
        String message,
        Integer statusCode,
        LocalDateTime timestamp) {
}
