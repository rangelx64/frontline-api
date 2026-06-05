package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.ClanMemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClanMemberRepositoryInterface {
    List<ClanMemberEntity> findByClanId(UUID clanId);

    List<ClanMemberEntity> findByUserId(UUID userId);

    Optional<ClanMemberEntity> findByClanIdAndUserId(UUID clanId, UUID userId);

    Page<ClanMemberEntity> findAll(Pageable pageable);

    Optional<ClanMemberEntity> findById(UUID id);
}
