package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.AccessLogEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessLogRepositoryInterface {
    List<AccessLogEntity> findByUserId(UUID user_id);

    List<AccessLogEntity> findByIpAddress(String ip_address);

    List<AccessLogEntity> findAll();

    Optional<AccessLogEntity> findById(Long id);
}
