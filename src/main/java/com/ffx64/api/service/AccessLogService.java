package com.ffx64.api.service;

import com.ffx64.api.dto.AccessLogRequestDTO;
import com.ffx64.api.dto.AccessLogResponseDTO;
import com.ffx64.api.entity.AccessLogEntity;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.repository.AccessLogRepository;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.service.interfaces.AccessLogServiceInterface;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public List<AccessLogResponseDTO> findAll() {
        return accessLogRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public Optional<AccessLogResponseDTO> findById(Long id) {
        return accessLogRepository.findById(id).map(this::toDTO);
    }

    @Override
    public List<AccessLogResponseDTO> findByUserId(UUID user_id) {
        return accessLogRepository
                .findByUserId(user_id)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AccessLogResponseDTO> findByIpAddress(String ip_address) {
        return accessLogRepository
                .findByIpAddress(ip_address)
                .stream()
                .map(this::toDTO)
                .toList();
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
