package com.ffx64.api.dto;

public record UserRequestDTO(
        String username,
        String bohemia_guid,
        String email,
        String password,
        Boolean accept_terms,
        String photo,
        String banner_photo,
        String description) {
}
