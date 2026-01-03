package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.ClanMemberEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClanMemberRepositoryInterface {
    List<ClanMemberEntity> findByClanId(UUID clanId);

    List<ClanMemberEntity> findByUserId(UUID userId);

    Optional<ClanMemberEntity> findByClanIdAndUserId(UUID clanId, UUID userId);

    List<ClanMemberEntity> findAll();

    Optional<ClanMemberEntity> findById(UUID id);
}
