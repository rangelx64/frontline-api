package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.dto.UserRequestDTO;
import com.ffx64.api.dto.UserResponseDTO;

public interface UserServiceInterface {
    UserResponseDTO get();

    UserResponseDTO update(UserRequestDTO userRequestDTO);

    void delete();

    PlayerStatsResponseDTO stats();
}
