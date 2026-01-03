package com.ffx64.api.exception;

public record ApiErrorResponseDTO(
        int status,
        String message,
        String timestamp) {
}
