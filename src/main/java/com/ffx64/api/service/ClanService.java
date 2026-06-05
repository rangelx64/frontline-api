package com.ffx64.api.service;

import com.ffx64.api.dto.ClanRequestDTO;
import com.ffx64.api.dto.ClanResponseDTO;
import com.ffx64.api.entity.ClanEntity;
import com.ffx64.api.exception.ClanExistsException;
import com.ffx64.api.repository.ClanRepository;
import com.ffx64.api.service.interfaces.ClanServiceInterface;
import java.time.LocalDateTime;
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
public class ClanService implements ClanServiceInterface {

    private final ClanRepository clanRepository;

    public ClanService(ClanRepository clanRepository) {
        this.clanRepository = clanRepository;
    }

    @Cacheable(value = "clans_find_all", key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public Page<ClanResponseDTO> findAll(Pageable pageable) {
        return clanRepository
                .findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Transactional
    @CacheEvict(value = { "clans_find_all", "clans_find_by_id", "clans_find_by_tag",
            "clans_find_by_name" }, allEntries = true)
    public ClanResponseDTO save(ClanRequestDTO clanRequestDTO) {
        clanRepository
                .findByTag(clanRequestDTO.tag())
                .ifPresent(clan -> {
                    throw new ClanExistsException();
                });
        clanRepository
                .findByName(clanRequestDTO.name())
                .ifPresent(clan -> {
                    throw new ClanExistsException();
                });

        ClanEntity clan = new ClanEntity();
        clan.setName(clanRequestDTO.name());
        clan.setTag(clanRequestDTO.tag());
        clan.setPhoto(clanRequestDTO.photo());
        clan.setDescription(clanRequestDTO.description());
        clan.setCreated_at(LocalDateTime.now());
        clan.setUpdated_at(LocalDateTime.now());
        ClanEntity savedClan = clanRepository.save(clan);

        return toResponseDTO(savedClan);
    }

    @Transactional
    @CacheEvict(value = { "clans_find_all", "clans_find_by_id", "clans_find_by_tag",
            "clans_find_by_name" }, allEntries = true)
    public Boolean deleteById(UUID id) {
        if (clanRepository.findById(id).isPresent()) {
            clanRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Cacheable(value = "clans_find_by_id", key = "#id")
    public Optional<ClanResponseDTO> findById(UUID id) {
        return clanRepository.findById(id).map(this::toResponseDTO);
    }

    @Cacheable(value = "clans_find_by_tag", key = "#tag")
    public Optional<ClanResponseDTO> findByTag(String tag) {
        return clanRepository.findByTag(tag).map(this::toResponseDTO);
    }

    @Cacheable(value = "clans_find_by_name", key = "#name")
    public Optional<ClanResponseDTO> findByName(String name) {
        return clanRepository.findByName(name).map(this::toResponseDTO);
    }

    private ClanResponseDTO toResponseDTO(ClanEntity clan) {
        return new ClanResponseDTO(
                clan.getId(),
                clan.getName(),
                clan.getTag(),
                clan.getPhoto(),
                clan.getDescription(),
                clan.getCreatedAt(),
                clan.getUpdatedAt());
    }
}
