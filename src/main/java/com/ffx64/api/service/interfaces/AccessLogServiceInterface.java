package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessLogServiceInterface {
    AccessLogResponseDTO save(AccessLogRequestDTO accessLogRequestDTO);

    List<AccessLogResponseDTO> findAll();

    Optional<AccessLogResponseDTO> findById(Long id);

    List<AccessLogResponseDTO> findByUserId(UUID user_id);

    List<AccessLogResponseDTO> findByIpAddress(String ip_address);
}
