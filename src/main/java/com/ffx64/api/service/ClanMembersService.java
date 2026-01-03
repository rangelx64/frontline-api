package com.ffx64.api.service;

import com.ffx64.api.dto.ClanMemberRequestDTO;
import com.ffx64.api.dto.ClanMemberResponseDTO;
import com.ffx64.api.entity.ClanEntity;
import com.ffx64.api.entity.ClanMemberEntity;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.exception.ClanMembersLimitException;
import com.ffx64.api.exception.ClanNotFoundException;
import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.ClanMembersRepository;
import com.ffx64.api.repository.ClanRepository;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.service.interfaces.ClanMemberServiceInterface;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
@Transactional(readOnly = true)
public class ClanMembersService implements ClanMemberServiceInterface {

    private final ClanMembersRepository clanMembersRepository;
    private final ClanRepository clanRepository;
    private final UserRepository userRepository;

    public ClanMembersService(
            ClanMembersRepository clanMembersRepository,
            ClanRepository clanRepository,
            UserRepository userRepository) {
        this.clanMembersRepository = clanMembersRepository;
        this.clanRepository = clanRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @CacheEvict(value = { "clan_members_find_all", "clan_members_find_by_clan_id",
            "clan_members_find_by_user_id" }, allEntries = true)
    public ClanMemberResponseDTO save(
            ClanMemberRequestDTO clanMembersRequestDTO) {
        ClanEntity clan = clanRepository
                .findById(clanMembersRequestDTO.clan_id())
                .orElseThrow(() -> new ClanNotFoundException());

        UserEntity user = userRepository
                .findById(clanMembersRequestDTO.user_id())
                .orElseThrow(() -> new UserNotFoundException());

        List<ClanMemberEntity> clanMemberss = clanMembersRepository.findByUserId(user.getId());

        if (!clanMemberss.isEmpty()) {
            throw new ClanMembersLimitException();
        }

        ClanMemberEntity clanMembers = new ClanMemberEntity();
        clanMembers.setClan(clan);
        clanMembers.setUser(user);
        clanMembers.setIsOwner(clanMembersRequestDTO.is_owner());
        clanMembers.setJoinedAt(clanMembersRequestDTO.joined_at());

        ClanMemberEntity savedClanMembers = clanMembersRepository.save(
                clanMembers);
        return toDTO(savedClanMembers);
    }

    @Cacheable(value = "clan_members_find_all")
    public List<ClanMemberResponseDTO> findAll() {
        return clanMembersRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = "clan_members_find_by_id", key = "#id")
    public Optional<ClanMemberResponseDTO> findById(UUID id) {
        return clanMembersRepository.findById(id).map(this::toDTO);
    }

    @Cacheable(value = "clan_members_find_by_clan_id", key = "#clanId")
    public List<ClanMemberResponseDTO> findByClanId(UUID clanId) {
        return clanMembersRepository
                .findByClanId(clanId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = "clan_members_find_by_user_id", key = "#userId")
    public List<ClanMemberResponseDTO> findByUserId(UUID userId) {
        return clanMembersRepository
                .findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ClanMemberResponseDTO toDTO(ClanMemberEntity member) {
        return new ClanMemberResponseDTO(
                member.getId(),
                member.getClan().getId(),
                member.getUser().getId(),
                member.getIsOwner(),
                member.getJoinedAt());
    }
}
