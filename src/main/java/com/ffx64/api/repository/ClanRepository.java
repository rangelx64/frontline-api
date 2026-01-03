package com.ffx64.api.repository;

import com.ffx64.api.entity.ClanEntity;
import com.ffx64.api.repository.interfaces.ClanRepositoryInterface;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanRepository
        extends JpaRepository<ClanEntity, UUID>, ClanRepositoryInterface {
    Optional<ClanEntity> findByTag(String tag);

    Optional<ClanEntity> findByName(String name);
}
