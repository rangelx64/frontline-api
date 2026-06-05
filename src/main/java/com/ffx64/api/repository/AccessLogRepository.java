package com.ffx64.api.repository;

import com.ffx64.api.entity.AccessLogEntity;
import com.ffx64.api.repository.interfaces.AccessLogRepositoryInterface;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository
        extends JpaRepository<AccessLogEntity, Long>, AccessLogRepositoryInterface {
    @Query("SELECT a FROM AccessLogEntity a WHERE a.user.id = :userId")
    Page<AccessLogEntity> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT a FROM AccessLogEntity a WHERE a.ip_address = :ipAddress")
    Page<AccessLogEntity> findByIpAddress(@Param("ipAddress") String ipAddress, Pageable pageable);

    Page<AccessLogEntity> findAll(Pageable pageable);

    Optional<AccessLogEntity> findById(Long id);
}
