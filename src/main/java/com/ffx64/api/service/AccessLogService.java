package com.ffx64.api.service;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import com.ffx64.api.entity.AccessLogEntity;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.repository.AccessLogRepository;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.service.interfaces.AccessLogServiceInterface;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccessLogService implements AccessLogServiceInterface {

    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;

    public AccessLogService(
            AccessLogRepository accessLogRepository,
            UserRepository userRepository) {
        this.accessLogRepository = accessLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = { "access_logs_find_all", "access_logs_find_by_id", "access_logs_find_by_user_id",
            "access_logs_find_by_ip_address" }, allEntries = true)
    public AccessLogResponseDTO save(AccessLogRequestDTO accessLogRequestDTO) {
        UserEntity user = null;
        if (accessLogRequestDTO.user_id() != null) {
            user = userRepository
                    .findById(accessLogRequestDTO.user_id())
                    .orElse(null);
        }

        AccessLogEntity accessLog = new AccessLogEntity();
        accessLog.setUser(user);
        accessLog.setIpAddress(accessLogRequestDTO.ip_address());
        accessLog.setUserAgent(accessLogRequestDTO.user_agent());
        accessLog.setMethod(accessLogRequestDTO.method());
        accessLog.setEndpoint(accessLogRequestDTO.endpoint());
        accessLog.setStatusCode(accessLogRequestDTO.status_code());
        accessLog.setResponseTimeMs(accessLogRequestDTO.response_time_ms());
        accessLog.setCreatedAt(accessLogRequestDTO.created_at());

        AccessLogEntity savedAccessLog = accessLogRepository.save(accessLog);
        return toDTO(savedAccessLog);
    }

    @Override
    @Cacheable(value = "access_logs_find_all", key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public Page<AccessLogResponseDTO> findAll(Pageable pageable) {
        return accessLogRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    @Cacheable(value = "access_logs_find_by_id", key = "#id")
    public Optional<AccessLogResponseDTO> findById(Long id) {
        return accessLogRepository.findById(id).map(this::toDTO);
    }

    @Override
    @Cacheable(value = "access_logs_find_by_user_id", key = "#userId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public Page<AccessLogResponseDTO> findByUserId(UUID userId, Pageable pageable) {
        return accessLogRepository
                .findByUserId(userId, pageable)
                .map(this::toDTO);
    }

    @Override
    @Cacheable(value = "access_logs_find_by_ip_address", key = "#ipAddress + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public Page<AccessLogResponseDTO> findByIpAddress(String ipAddress, Pageable pageable) {
        return accessLogRepository
                .findByIpAddress(ipAddress, pageable)
                .map(this::toDTO);
    }

    private AccessLogResponseDTO toDTO(AccessLogEntity accessLog) {
        return new AccessLogResponseDTO(
                accessLog.getId(),
                accessLog.getUser() != null ? accessLog.getUser().getId() : null,
                accessLog.getIpAddress(),
                accessLog.getUserAgent(),
                accessLog.getMethod(),
                accessLog.getEndpoint(),
                accessLog.getStatusCode(),
                accessLog.getResponseTimeMs(),
                accessLog.getCreatedAt());
    }
}
