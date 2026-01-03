package com.ffx64.api.repository;

import com.ffx64.api.entity.AccessLogEntity;
import com.ffx64.api.repository.interfaces.AccessLogRepositoryInterface;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository
        extends JpaRepository<AccessLogEntity, Long>, AccessLogRepositoryInterface {
    @Query("SELECT a FROM AccessLogEntity a WHERE a.user.id = :user_id")
    List<AccessLogEntity> findByUserId(@Param("user_id") UUID user_id);

    @Query("SELECT a FROM AccessLogEntity a WHERE a.ip_address = :ip_address")
    List<AccessLogEntity> findByIpAddress(
            @Param("ip_address") String ip_address);

    List<AccessLogEntity> findAll();

    Optional<AccessLogEntity> findById(Long id);
}
