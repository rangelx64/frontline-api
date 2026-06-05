package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessLogServiceInterface {
    AccessLogResponseDTO save(AccessLogRequestDTO accessLogRequestDTO);

    Page<AccessLogResponseDTO> findAll(Pageable pageable);

    Optional<AccessLogResponseDTO> findById(Long id);

    Page<AccessLogResponseDTO> findByUserId(UUID userId, Pageable pageable);

    Page<AccessLogResponseDTO> findByIpAddress(String ipAddress, Pageable pageable);
}
