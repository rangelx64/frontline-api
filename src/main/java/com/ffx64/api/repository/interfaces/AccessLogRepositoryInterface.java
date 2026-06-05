package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.AccessLogEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessLogRepositoryInterface {
    Page<AccessLogEntity> findByUserId(UUID userId, Pageable pageable);

    Page<AccessLogEntity> findByIpAddress(String ipAddress, Pageable pageable);

    Page<AccessLogEntity> findAll(Pageable pageable);

    Optional<AccessLogEntity> findById(Long id);
}
