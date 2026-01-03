package com.ffx64.api.repository;

import com.ffx64.api.entity.ClanMemberEntity;
import com.ffx64.api.repository.interfaces.ClanMemberRepositoryInterface;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanMembersRepository
        extends
        JpaRepository<ClanMemberEntity, UUID>, ClanMemberRepositoryInterface {
    List<ClanMemberEntity> findByClanId(UUID clanId);

    List<ClanMemberEntity> findByUserId(UUID userId);

    Optional<ClanMemberEntity> findByClanIdAndUserId(UUID clanId, UUID userId);

    List<ClanMemberEntity> findAll();

    Optional<ClanMemberEntity> findById(UUID id);
}
